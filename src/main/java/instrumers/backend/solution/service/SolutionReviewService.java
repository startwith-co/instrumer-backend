package instrumers.backend.solution.service;

import instrumers.backend.common.dto.PageInfo;
import instrumers.backend.exception.BadRequestException;
import instrumers.backend.exception.NotFoundException;
import instrumers.backend.solution.domain.SolutionReviewEntity;
import instrumers.backend.solution.repository.SolutionReviewRepository;
import instrumers.backend.solution.domain.SolutionEntity;
import instrumers.backend.solution.repository.SolutionRepository;
import instrumers.backend.user.consumer.model.ConsumerEntity;
import instrumers.backend.user.consumer.repository.ConsumerRepository;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import instrumers.backend.user.user.util.UserType;
import instrumers.backend.user.vendor.model.VendorEntity;
import instrumers.backend.user.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static instrumers.backend.solution.controller.request.SolutionReviewRequest.*;
import static instrumers.backend.solution.controller.response.SolutionReviewResponse.*;
import static instrumers.backend.solution.controller.response.SolutionReviewResponse.GetSolutionReviewPageResponse.*;

@Service
@RequiredArgsConstructor
public class SolutionReviewService {
    private final UserRepository userRepository;
    private final SolutionRepository solutionRepository;
    private final SolutionReviewRepository solutionReviewRepository;
    private final ConsumerRepository consumerRepository;
    private final VendorRepository vendorRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CREATE_REVIEW_LOCK_KEY_FMT = "lock:create:review:%s:%s";
    private static final long LOCK_TIMEOUT_SECONDS = 10;

    @Transactional
    public CreateSolutionReviewResponse create(Long userSeq, Long solutionSeq, CreateSolutionReviewRequest request) {
        // Redis 분산 락 획득 시도 (동일 사용자가 동일 솔루션에 중복 리뷰 작성 방지)
        String lockKey = String.format(CREATE_REVIEW_LOCK_KEY_FMT, userSeq, solutionSeq);
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                "locked",
                Duration.ofSeconds(LOCK_TIMEOUT_SECONDS)
        );

        if (!Boolean.TRUE.equals(lockAcquired)) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "이미 처리 중인 요청입니다. 잠시 후 다시 시도해주세요."
            );
        }

        try {
            UserEntity userEntity = userRepository.findById(userSeq)
                    .orElseThrow(() -> new NotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            "존재하지 않는 회원입니다."
                    ));
            SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(solutionSeq)
                    .orElseThrow(() -> new NotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            "존재하지 않는 솔루션입니다."
                    ));

            if (request.context().length() > 240) {
                throw new BadRequestException(
                        HttpStatus.BAD_REQUEST.value(),
                        "리뷰 글자수는 240자가 최대입니다."
                );
            }

            SolutionReviewEntity solutionReviewEntity = solutionReviewRepository.save(
                    SolutionReviewEntity.builder()
                            .context(request.context())
                            .rate(request.rate())
                            .solutionEntity(solutionEntity)
                            .userEntity(userEntity)
                            .build()
            );

            return new CreateSolutionReviewResponse(solutionReviewEntity.getSolutionReviewSeq());
        } finally {
            // 락 해제 (성공/실패 관계없이)
            redisTemplate.delete(lockKey);
        }
    }

    @Transactional(readOnly = true)
    public GetSolutionReviewPageResponse get(Long solutionSeq, Pageable pageable) {
        SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(solutionSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 솔루션입니다."
                ));

        Page<SolutionReviewEntity> reviewPage = solutionReviewRepository.findAllBySolutionEntity(solutionEntity, pageable);
        List<SolutionReviewEntity> reviews = reviewPage.getContent();

        Set<UserEntity> userEntities = reviews.stream()
                .map(SolutionReviewEntity::getUserEntity)
                .collect(Collectors.toSet());

        Map<Long, String> consumerBusinessNameMap = consumerRepository.findAllByUserEntityIn(
                userEntities.stream().filter(u -> u.getUserType() == UserType.CONSUMER).toList()
        ).stream().collect(Collectors.toMap(
                c -> c.getUserEntity().getUserSeq(),
                ConsumerEntity::getBusinessName
        ));

        Map<Long, String> vendorBusinessNameMap = vendorRepository.findAllByUserEntityIn(
                userEntities.stream().filter(u -> u.getUserType() == UserType.VENDOR).toList()
        ).stream().collect(Collectors.toMap(
                v -> v.getUserEntity().getUserSeq(),
                VendorEntity::getBusinessName
        ));

        List<GetSolutionReviewResponse> content = reviews.stream()
                .map(review -> {
                    UserEntity userEntity = review.getUserEntity();
                    String businessName = null;

                    if (userEntity.getUserType() == UserType.CONSUMER) {
                        businessName = consumerBusinessNameMap.get(userEntity.getUserSeq());
                    } else if (userEntity.getUserType() == UserType.VENDOR) {
                        businessName = vendorBusinessNameMap.get(userEntity.getUserSeq());
                    }

                    return new GetSolutionReviewResponse(
                            review.getSolutionReviewSeq(), userEntity.getProfileImageUrl(), businessName,
                            review.getRate(), review.getContext(), review.getCreatedAt()
                    );
                })
                .toList();

        PageInfo pageInfo = PageInfo.from(reviewPage);

        return new GetSolutionReviewPageResponse(content, pageInfo);
    }
}

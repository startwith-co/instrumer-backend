package instrumers.backend.solution.service;

import instrumers.backend.exception.BadRequestException;
import instrumers.backend.exception.NotFoundException;
import instrumers.backend.solution.domain.SolutionImageEntity;
import instrumers.backend.solution.repository.*;
import instrumers.backend.solution.domain.SolutionKeywordEntity;
import instrumers.backend.solution.domain.SolutionPlanDetailEntity;
import instrumers.backend.solution.domain.SolutionPlanEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import instrumers.backend.user.vendor.model.VendorEntity;
import instrumers.backend.user.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import instrumers.backend.common.dto.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static instrumers.backend.solution.controller.request.SolutionRequest.*;
import static instrumers.backend.solution.controller.response.SolutionResponse.*;
import static instrumers.backend.solution.controller.response.SolutionResponse.GetSolutionListResponse.*;
import static instrumers.backend.solution.controller.response.SolutionResponse.GetSolutionResponse.*;
import static instrumers.backend.solution.controller.response.SolutionVendorResponse.*;

@Service
@RequiredArgsConstructor
public class SolutionService {
    private final UserRepository userRepository;
    private final SolutionRepository solutionRepository;
    private final SolutionImageRepository solutionImageRepository;
    private final SolutionPlanRepository solutionPlanRepository;
    private final SolutionPlanDetailRepository solutionPlanDetailRepository;
    private final SolutionKeywordRepository solutionKeywordRepository;
    private final VendorRepository vendorRepository;
    private final SolutionReviewRepository solutionReviewRepository;

    @Transactional
    public CreateSolutionResponse create(Long userSeq, CreateSolutionRequest request) {
        if (request.images() == null || request.images().isEmpty()) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "대표이미지 및 솔루션 상세 설명 PDF는 필수 입니다."
            );
        }

        UserEntity userEntity = userRepository.findByUserSeqLock(userSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 회원입니다."
                ));

        SolutionEntity solutionEntity = solutionRepository.save(SolutionEntity.builder()
                .name(request.name())
                .explanation(request.explanation())
                .category(request.category())
                .price(request.price())
                .userEntity(userEntity)
                .build());

        request.images().forEach(image -> solutionImageRepository.save(SolutionImageEntity.builder()
                .imageUrl(image.imageUrl())
                .imageType(image.imageType())
                .solutionEntity(solutionEntity)
                .build()));

        if (request.plans() != null) {
            request.plans().forEach(plan -> {
                SolutionPlanEntity solutionPlanEntity = solutionPlanRepository.save(SolutionPlanEntity.builder()
                        .name(plan.name())
                        .subName(plan.subName())
                        .price(plan.price())
                        .planType(plan.planType())
                        .solutionEntity(solutionEntity)
                        .build());

                if (plan.details() != null) {
                    plan.details()
                            .forEach(detail -> solutionPlanDetailRepository.save(SolutionPlanDetailEntity.builder()
                                    .name(detail.name())
                                    .context(detail.context())
                                    .solutionPlanEntity(solutionPlanEntity)
                                    .build()));
                }
            });
        }

        if (request.keywords() != null) {
            request.keywords().forEach(keyword -> solutionKeywordRepository.save(SolutionKeywordEntity.builder()
                    .keyword(keyword)
                    .solutionEntity(solutionEntity)
                    .build()));
        }

        return new CreateSolutionResponse(solutionEntity.getSolutionSeq());
    }

    @Transactional
    public CreateSolutionResponse update(Long userSeq, UpdateSolutionRequest request) {
        SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(request.solutionSeq())
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 솔루션입니다."
                ));

        if (!solutionEntity.getUserEntity().getUserSeq().equals(userSeq)) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "본인의 솔루션만 수정할 수 있습니다."
            );
        }

        solutionRepository.delete(solutionEntity);

        CreateSolutionRequest createRequest = new CreateSolutionRequest(
                request.name(),
                request.explanation(),
                request.category(),
                request.price(),
                request.images() != null ? request.images().stream()
                        .map(image -> new CreateSolutionRequest.CreateSolutionImageRequest(
                                image.imageUrl(),
                                image.imageType()
                        ))
                        .toList() : null,
                request.plans() != null ? request.plans().stream()
                        .map(plan -> new CreateSolutionRequest.CreateSolutionPlanRequest(
                                plan.name(),
                                plan.subName(),
                                plan.price(),
                                plan.planType(),
                                plan.details() != null ? plan.details().stream()
                                        .map(detail -> new CreateSolutionRequest.CreateSolutionPlanDetailRequest(
                                                detail.name(),
                                                detail.context()
                                        ))
                                        .toList() : null
                        ))
                        .toList() : null,
                request.keywords()
        );

        create(userSeq, createRequest);

        return new CreateSolutionResponse(solutionEntity.getSolutionSeq());
    }

    @Transactional(readOnly = true)
    public GetSolutionResponse getSolution(Long solutionSeq) {
        SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(solutionSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 솔루션입니다."
                ));

        // 리뷰 정보 조회
        long reviewCount = solutionReviewRepository.countBySolutionEntity(solutionEntity);
        Double averageRate = solutionReviewRepository.getAverageRateBySolutionEntity(solutionEntity);
        double average = Math.round((averageRate != null ? averageRate : 0.0) * 10.0) / 10.0;
        GetSolutionReviewInfo reviewInfo = new GetSolutionReviewInfo(reviewCount, average);

        // 벤더 정보 조회
        VendorEntity vendorEntity = vendorRepository.findByUserEntity(solutionEntity.getUserEntity())
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 기업 회원입니다."
                ));
        GetSolutionVendorInfo vendorInfo = new GetSolutionVendorInfo(
                vendorEntity.getVendorSeq(),
                vendorEntity.getBusinessName()
        );

        return new GetSolutionResponse(
                solutionEntity.getSolutionSeq(),
                solutionEntity.getName(),
                solutionEntity.getExplanation(),
                solutionEntity.getCategory(),
                solutionEntity.getPrice(),
                solutionImageRepository.findAllBySolutionEntity(solutionEntity).stream()
                        .map(image -> new GetSolutionImageRequest(image.getImageUrl(), image.getImageType()))
                        .toList(),
                solutionPlanRepository.findAllBySolutionEntity(solutionEntity).stream()
                        .map(plan -> new GetSolutionPlanRequest(
                                plan.getName(),
                                plan.getSubName(),
                                plan.getPrice(),
                                plan.getPlanType(),
                                solutionPlanDetailRepository.findAllBySolutionPlanEntity(plan).stream()
                                        .map(detail -> new GetSolutionPlanDetailRequest(detail.getName(), detail.getContext()))
                                        .toList()
                        ))
                        .toList(),
                solutionKeywordRepository.findAllBySolutionEntity(solutionEntity).stream()
                        .map(SolutionKeywordEntity::getKeyword)
                        .toList(),
                reviewInfo,
                vendorInfo
        );
    }

    @Transactional
    public void delete(Long userSeq, Long solutionSeq) {
        userRepository.findById(userSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 회원입니다."
                ));
        SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(solutionSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 솔루션입니다."
                ));

        solutionRepository.delete(solutionEntity);
    }

    @Transactional(readOnly = true)
    public GetSolutionListResponse getSolutionList(String category, Long minPrice, Long maxPrice, Pageable pageable) {
        Page<GetSolutionList> page = solutionRepository.findSolutionList(category, minPrice, maxPrice, pageable);
        PageInfo pageInfo = PageInfo.from(page);

        return new GetSolutionListResponse(page.getContent(), pageInfo);
    }
}

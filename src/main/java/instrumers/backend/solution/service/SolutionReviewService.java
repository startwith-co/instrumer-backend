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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

	@Transactional
	public CreateSolutionReviewResponse create(Long userSeq, CreateSolutionReviewRequest request) {
		UserEntity userEntity = userRepository.findByUserSeqLock(userSeq)
			.orElseThrow(() -> new NotFoundException(
				HttpStatus.NOT_FOUND.value(),
				"존재하지 않는 회원입니다."
			));
		SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(request.solutionSeq())
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

		SolutionReviewEntity solutionReviewEntity = SolutionReviewEntity.builder()
			.context(request.context())
			.rate(request.rate())
			.solutionEntity(solutionEntity)
			.userEntity(userEntity)
			.build();
		solutionReviewRepository.save(solutionReviewEntity);

		return new CreateSolutionReviewResponse(solutionReviewEntity.getSolutionReviewSeq());
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

		// N+1 문제 방지: 모든 userEntity를 한 번에 조회
		java.util.Set<UserEntity> userEntities = reviews.stream()
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

package instrumers.backend.solution.solution.service;

import instrumers.backend.exception.BadRequestException;
import instrumers.backend.exception.NotFoundException;
import instrumers.backend.solution.image.model.SolutionImageEntity;
import instrumers.backend.solution.image.repository.SolutionImageRepository;
import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import instrumers.backend.solution.keyword.repository.SolutionKeywordRepository;
import instrumers.backend.solution.plan.detail.model.SolutionPlanDetailEntity;
import instrumers.backend.solution.plan.detail.repository.SolutionPlanDetailRepository;
import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import instrumers.backend.solution.plan.repository.SolutionPlanRepository;
import instrumers.backend.solution.solution.model.SolutionEntity;
import instrumers.backend.solution.solution.repository.SolutionRepository;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static instrumers.backend.solution.solution.controller.request.SolutionRequest.*;
import static instrumers.backend.solution.solution.controller.response.SolutionResponse.*;
import static instrumers.backend.solution.solution.controller.response.SolutionResponse.GetSolutionResponse.*;

@Service
@RequiredArgsConstructor
public class SolutionService {
    private final UserRepository userRepository;
    private final SolutionRepository solutionRepository;
    private final SolutionImageRepository solutionImageRepository;
    private final SolutionPlanRepository solutionPlanRepository;
    private final SolutionPlanDetailRepository solutionPlanDetailRepository;
    private final SolutionKeywordRepository solutionKeywordRepository;

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
                    plan.details().forEach(detail -> solutionPlanDetailRepository.save(SolutionPlanDetailEntity.builder()
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
    public GetSolutionResponse get(Long userSeq, Long solutionSeq) {
        userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 회원입니다."
                ));

        SolutionEntity solutionEntity = solutionRepository.findBySolutionSeq(solutionSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 솔루션입니다."
                ));

        return new GetSolutionResponse(
                solutionEntity.getName(),
                solutionEntity.getExplanation(),
                solutionEntity.getCategory(),
                solutionEntity.getPrice(),
                solutionImageRepository.findAllBySolutionEntity(solutionEntity).stream()
                        .map(image -> new CreateSolutionImageRequest(image.getImageUrl(), image.getImageType()))
                        .toList(),
                solutionPlanRepository.findAllBySolutionEntity(solutionEntity).stream()
                        .map(plan -> new CreateSolutionPlanRequest(
                                plan.getName(),
                                plan.getSubName(),
                                plan.getPrice(),
                                plan.getPlanType(),
                                solutionPlanDetailRepository.findAllBySolutionPlanEntity(plan).stream()
                                        .map(detail -> new CreateSolutionPlanDetailRequest(detail.getName(), detail.getContext()))
                                        .toList()
                        ))
                        .toList(),
                solutionKeywordRepository.findAllBySolutionEntity(solutionEntity).stream()
                        .map(SolutionKeywordEntity::getKeyword)
                        .toList()
        );
    }
}

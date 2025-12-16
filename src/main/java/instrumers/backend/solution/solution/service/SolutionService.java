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
                .userEntity(userEntity)
                .build());

        request.images().forEach(image -> solutionImageRepository.save(SolutionImageEntity.builder()
                .imageUrl(image.imageUrl())
                .imageType(image.imageType())
                .solution(solutionEntity)
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
}

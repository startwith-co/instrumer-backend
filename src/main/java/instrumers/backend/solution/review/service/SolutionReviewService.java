package instrumers.backend.solution.review.service;

import instrumers.backend.exception.NotFoundException;
import instrumers.backend.solution.review.controller.response.SolutionReviewResponse;
import instrumers.backend.solution.review.model.SolutionReviewEntity;
import instrumers.backend.solution.review.repository.SolutionReviewRepository;
import instrumers.backend.solution.solution.model.SolutionEntity;
import instrumers.backend.solution.solution.repository.SolutionRepository;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static instrumers.backend.solution.review.controller.request.SolutionReviewRequest.*;
import static instrumers.backend.solution.review.controller.response.SolutionReviewResponse.*;

@Service
@RequiredArgsConstructor
public class SolutionReviewService {
    private final UserRepository userRepository;
    private final SolutionRepository solutionRepository;
    private final SolutionReviewRepository solutionReviewRepository;

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


        SolutionReviewEntity solutionReviewEntity = SolutionReviewEntity.builder()
                .context(request.context())
                .rate(request.rate())
                .solutionEntity(solutionEntity)
                .userEntity(userEntity)
                .build();
        solutionReviewRepository.save(solutionReviewEntity);

        return new CreateSolutionReviewResponse(solutionReviewEntity.getSolutionReviewSeq());
    }
}

package instrumers.backend.solution.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.solution.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.solution.solution.controller.request.SolutionRequest.*;
import static instrumers.backend.solution.solution.controller.response.SolutionResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solution-service")
@Tag(name = "솔루션")
public class SolutionController {
    private final SolutionService solutionService;

    @PostMapping()
    @Operation(summary = "솔루션 정보 저장")
    public ResponseEntity<BaseResponse<CreateSolutionResponse>> create(HttpServletRequest httpServletRequest, @Valid @RequestBody CreateSolutionRequest request) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        CreateSolutionResponse response = solutionService.create(userSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PutMapping()
    @Operation(summary = "솔루션 정보 수정")
    public ResponseEntity<BaseResponse<CreateSolutionResponse>> update(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateSolutionRequest request) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        CreateSolutionResponse response = solutionService.update(userSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @GetMapping()
    @Operation(summary = "솔루션 개별 조회")
    public ResponseEntity<BaseResponse<GetSolutionResponse>> get(HttpServletRequest httpServletRequest, @RequestParam(value = "solutionSeq") Long solutionSeq) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        GetSolutionResponse response = solutionService.get(userSeq, solutionSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @DeleteMapping()
    @Operation(summary = "솔루션 삭제")
    public ResponseEntity<BaseResponse<String>> delete(HttpServletRequest httpServletRequest, @RequestParam(value = "solutionSeq") Long solutionSeq) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        solutionService.delete(userSeq, solutionSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}

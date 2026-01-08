package instrumers.backend.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.solution.controller.request.SolutionRequest.*;
import static instrumers.backend.solution.controller.response.SolutionResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendor/solutions")
@Tag(name = "솔루션 API (Vendor)")
public class SolutionVendorController {

    private final SolutionService solutionService;

    @PostMapping
    @Operation(summary = "솔루션 생성")
    public ResponseEntity<BaseResponse<CreateSolutionResponse>> createSolution(
            HttpServletRequest request,
            @Valid @RequestBody CreateSolutionRequest dto
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        CreateSolutionResponse response = solutionService.create(userSeq, dto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PutMapping("/{solutionSeq}")
    @Operation(summary = "솔루션 수정")
    public ResponseEntity<BaseResponse<String>> updateSolution(
            HttpServletRequest httpServletRequest,
            @PathVariable Long solutionSeq,
            @Valid @RequestBody UpdateSolutionRequest request
    ) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        solutionService.update(userSeq, solutionSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @DeleteMapping("/{solutionSeq}")
    @Operation(summary = "솔루션 삭제")
    public ResponseEntity<BaseResponse<String>> deleteSolution(
            HttpServletRequest httpServletRequest,
            @PathVariable Long solutionSeq
    ) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        solutionService.delete(userSeq, solutionSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}

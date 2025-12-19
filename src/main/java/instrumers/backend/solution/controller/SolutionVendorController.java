package instrumers.backend.solution.controller;

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
@RequestMapping("/api/vendor/solutions")
@Tag(name = "솔루션 API (Vendor)")
public class SolutionVendorController {

    private final SolutionService solutionService;

    /**
     * 솔루션 생성
     * - VENDOR 권한 필요
     * - 자동으로 현재 로그인한 vendor에 매핑
     */
    @PostMapping
    @Operation(summary = "솔루션 생성")
    public ResponseEntity<BaseResponse<CreateSolutionResponse>> createSolution(
            HttpServletRequest request,
            @Valid @RequestBody CreateSolutionRequest dto) {

        Long userSeq = (Long) request.getAttribute("userSeq");
        CreateSolutionResponse response = solutionService.create(userSeq, dto);

        return ResponseEntity.ok()
                .body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    /**
     * 솔루션 수정
     * - VENDOR 권한 필요
     * - 소유권 체크: 자신이 생성한 솔루션만 수정 가능
     */
    @PutMapping
    @Operation(summary = "솔루션 수정")
    public ResponseEntity<BaseResponse<CreateSolutionResponse>> updateSolution(
            HttpServletRequest request,
            @Valid @RequestBody UpdateSolutionRequest dto) {

        Long userSeq = (Long) request.getAttribute("userSeq");

        // 소유권 체크는 Service 레이어에서 수행
        CreateSolutionResponse response = solutionService.update(userSeq, dto);

        return ResponseEntity.ok()
                .body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    /**
     * 솔루션 삭제
     * - VENDOR 권한 필요
     * - 소유권 체크: 자신이 생성한 솔루션만 삭제 가능
     */
    @DeleteMapping
    @Operation(summary = "솔루션 삭제")
    public ResponseEntity<BaseResponse<String>> deleteSolution(
            HttpServletRequest request,
            @RequestParam(value = "solutionSeq") Long solutionSeq) {

        Long userSeq = (Long) request.getAttribute("userSeq");

        // 소유권 체크 포함
        solutionService.delete(userSeq, solutionSeq);

        return ResponseEntity.ok()
                .body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}

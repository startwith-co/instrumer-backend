package instrumers.backend.user.vendor.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.user.vendor.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.user.vendor.controller.request.VendorRequest.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendor-service")
@Tag(name = "벤더 기업")
public class VendorController {
    private final VendorService vendorService;

    @PostMapping(value = "/auth/register")
    @Operation(summary = "벤더 기업 회원가입")
    public ResponseEntity<BaseResponse<String>> register(@Valid @RequestBody RegisterVendorRequest request) {
        vendorService.save(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PutMapping
    @Operation(summary = "벤더 기업 회원 정보 수정")
    public ResponseEntity<BaseResponse<String>> update(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateVendorRequest request) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        vendorService.update(userSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}

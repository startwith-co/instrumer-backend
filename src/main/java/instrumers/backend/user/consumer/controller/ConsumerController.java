package instrumers.backend.user.consumer.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.user.consumer.controller.response.ConsumerResponse;
import instrumers.backend.user.consumer.service.ConsumerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.user.consumer.controller.request.ConsumerRequest.*;
import static instrumers.backend.user.consumer.controller.response.ConsumerResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumer")
@Tag(name = "수요 기업")
public class ConsumerController {
    private final ConsumerService consumerService;

    @PutMapping()
    @Operation(summary = "수요 고객 정보 수정")
    public ResponseEntity<BaseResponse<String>> update(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateConsumerRequest request) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        consumerService.update(userSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @GetMapping
    @Operation(summary = "수요 고객 정보 조회")
    public ResponseEntity<BaseResponse<GetConsumerResponse>> get(HttpServletRequest httpServletRequest) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        GetConsumerResponse response = consumerService.get(userSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }
}

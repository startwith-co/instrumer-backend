package instrumers.backend.user.consumer.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.user.consumer.service.ConsumerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static instrumers.backend.user.consumer.controller.request.ConsumerRequest.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumer-service")
@Tag(name = "수요 기업")
public class ConsumerController {
    private final ConsumerService consumerService;

    @PostMapping(value = "/auth/register")
    @Operation(summary = "수요 고객 회원가입")
    public ResponseEntity<BaseResponse<String>> register(@Valid @RequestBody RegisterConsumerRequest request) {
        consumerService.save(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}

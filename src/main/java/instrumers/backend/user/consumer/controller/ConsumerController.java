package instrumers.backend.user.consumer.controller;

import instrumers.backend.user.consumer.service.ConsumerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumer")
@Tag(name = "수요 기업")
public class ConsumerController {
    private final ConsumerService consumerService;
}

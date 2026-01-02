package instrumers.backend.user.vendor.controller;

import instrumers.backend.user.vendor.service.VendorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendor")
@Tag(name = "벤더 기업")
public class VendorController {
    private final VendorService vendorService;
}

package instrumers.backend.user.vendor.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VendorRequest {
    public record RegisterVendorRequest(
            @NotBlank(message = "사업자명은 필수 입력값입니다.")
            String businessName,
            
            @NotBlank(message = "담당자명은 필수 입력값입니다.")
            String managerName,
            
            @NotBlank(message = "전화번호는 필수 입력값입니다.")
            String phone,
            
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String email,
            
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            String password,
            
            @NotBlank(message = "사업자 등록증 이미지는 필수 입력값입니다.")
            String businessImage
    ) {
    }
}

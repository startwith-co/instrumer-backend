package instrumers.backend.user.vendor.controller.response;

import instrumers.backend.user.user.util.UserType;

public class VendorResponse {
    public record GetVendorResponse(
            Long userSeq,
            String email,
            UserType userType,
            String profileImageUrl,
            String businessName,
            String managerName,
            String phone,
            String bank,
            String account
    ) {
    }
}

package instrumers.backend.user.consumer.controller.response;

import instrumers.backend.user.user.util.UserType;

public class ConsumerResponse {
    public record GetConsumerResponse(
            Long userSeq,
            String email,
            UserType userType,
            String profileImageUrl,
            String businessName,
            String managerName,
            String phone
    ) {
    }
}

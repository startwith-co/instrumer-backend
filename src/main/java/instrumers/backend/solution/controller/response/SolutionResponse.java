package instrumers.backend.solution.controller.response;

import instrumers.backend.common.dto.PageInfo;

import java.util.List;

public class SolutionResponse {
    public record CreateSolutionResponse(
            Long solutionSeq
    ) {
    }

    public record GetSolutionResponse(
            Long solutionSeq,
            String name,
            String explanation,
            String category,
            Long price,
            List<GetSolutionImageRequest> images,
            List<GetSolutionPlanRequest> plans,
            Long reviewCnt,
            Double reviewAverage,
            Long vendorSeq,
            String vendorBusinessName,
            String profileImageUrl
    ) {
        public record GetSolutionImageRequest(
                String imageUrl,
                String imageType
        ) {
        }

        public record GetSolutionPlanRequest(
                Long solutionPlanSeq,
                String name,
                String subName,
                Long price,
                String planType,
                List<GetSolutionPlanDetailRequest> details
        ) {
        }

        public record GetSolutionPlanDetailRequest(
                String name,
                String context
        ) {
        }
    }

    public record GetSolutionListResponse(
            List<GetSolutionList> content,
            PageInfo page
    ) {
        public record GetSolutionList(
                Long solutionSeq,
                String image,
                String name,
                Long price,
                Long cnt,
                Double average,
                String businessName
        ) {
        }
    }
}

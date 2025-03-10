package danpoong.soenter.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ReviewDTO {
    public static class ReviewRequest {

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PostReviewRequest {
            @NotNull
            private Long enterpriseId;
            private String title;
            private String content;
            private LocalDate visitDate;
            private List<Integer> tagNumbers;

        }
    }

    public static class ReviewResponse {
        @Getter
        @Builder
        public static class PostReviewResponse {
            private Long reviewId;
            private String content;
            private LocalDate createAt;
        }

        @Getter
        @Builder
        public static class GetMyReviewResponse {
            private Long reviewId;
            private String enterpriseName;
            private String enterpriseAddress;
            private String socialPurpose;
            private String content;
            private LocalDate createAt;
            private Integer tagCount;
            private List<Integer> tagNumbers;
        }

        @Getter
        @Builder
        public static class MyReviewsWrapperResponse {
            private String userName;
            private Integer totalReviewCount;
            private List<GetMyReviewResponse> reviews;
        }

        @Getter
        @Builder
        public static class GetEnterpriseReviewResponse {
            private Long reviewId;
            private String socialPurpose;
            private String userName;
            private String content;
            private LocalDate createAt;
            private Integer tagCount;
            private List<Integer> tagNumbers;
        }
    }
}

package danpoong.soenter.domain.review.service;

import danpoong.soenter.domain.enterprise.entity.Enterprise;
import danpoong.soenter.domain.enterprise.entity.Visit;
import danpoong.soenter.domain.enterprise.repository.EnterpriseRepository;
import danpoong.soenter.domain.enterprise.repository.VisitRepository;
import danpoong.soenter.domain.review.converter.ReviewConverter;
import danpoong.soenter.domain.review.entity.Review;
import danpoong.soenter.domain.review.entity.TagList;
import danpoong.soenter.domain.review.repository.ReviewRepository;
import danpoong.soenter.domain.review.repository.TagListRepository;
import danpoong.soenter.domain.user.entity.User;
import danpoong.soenter.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import danpoong.soenter.domain.review.dto.ReviewDTO.ReviewRequest.PostReviewRequest;
import danpoong.soenter.domain.review.dto.ReviewDTO.ReviewResponse.PostReviewResponse;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final TagListRepository tagListRepository;

    @Transactional
    public PostReviewResponse createReview(PostReviewRequest postReviewRequest, String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        Enterprise enterprise = enterpriseRepository.findById(postReviewRequest.getEnterpriseId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 기업입니다."));

        // Visit 기록 저장
        Visit visit = ReviewConverter.toVisit(user, enterprise, postReviewRequest.getVisitDate());
        visitRepository.save(visit);

        // Review 저장
        Review review = ReviewConverter.toReview(user, enterprise, postReviewRequest);
        reviewRepository.save(review);

        // TagList 저장
        postReviewRequest.getTagNumbers().forEach(tagNum -> {
            TagList tag = ReviewConverter.toTagList(review, tagNum);
            tagListRepository.save(tag);
        });

        // 저장된 TagList 개수 계산 후 Review의 tagNum 업데이트
        int tagCount = postReviewRequest.getTagNumbers().size();
        review.updateTagNum(tagCount);
        reviewRepository.save(review);

        return ReviewConverter.toReviewResponseDto(review);
    }
}

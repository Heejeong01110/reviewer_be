package org.movie.reviewer.domain.review_comment.dto;

import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.dto.response.UserCommentResponse;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;
import org.springframework.stereotype.Component;

@Component
public class ReviewCommentConverter {

  public static ReviewCommentResponse toReviewCommentResponse(ReviewComment reviewComment) {
    return ReviewCommentResponse.builder()
        .id(reviewComment.getId())
        .contents(reviewComment.getContents())
        .likeCount(reviewComment.getLikeCount())
        .updatedAt(reviewComment.getUpdatedAt())
        .user(
            UserSimpleInfo.builder()
                .id(reviewComment.getUser().getId())
                .nickname(reviewComment.getUser().getNickname())
                .profileImage(reviewComment.getUser().getProfileImage())
                .build())
        .build();
  }

  public static UserCommentResponse toUserCommentResponse(ReviewComment reviewComment) {
    return UserCommentResponse.builder()
        .id(reviewComment.getId())
        .contents(reviewComment.getContents())
        .likeCount(reviewComment.getLikeCount())
        .updatedAt(reviewComment.getUpdatedAt())
        .reviewId(reviewComment.getReview().getId())
        .build();
  }
}

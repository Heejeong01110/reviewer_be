package org.movie.reviewer.domain.review.dto;

import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewSimpleResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

  public static ReviewTitleResponse toReviewTitleResponse(ReviewTitleInfo response) {
    return ReviewTitleResponse.builder()
        .id(response.getReviewId())
        .title(response.getReviewTitle())
        .commentCount(response.getCommentCount())
        .user(
            UserSimpleInfo.builder()
                .id(response.getUserId())
                .nickname(response.getNickname())
                .profileImage(response.getProfileImage())
                .build())
        .movie(
            MovieSimpleInfo.builder()
                .id(response.getMovieId())
                .title(response.getMovieTitle())
                .movieImage(response.getMovieImage())
                .build())
        .build();
  }

  public static ReviewDetailResponse toReviewDetailResponse(Review review, Double rating) {
    return ReviewDetailResponse.builder()
        .id(review.getId())
        .title(review.getTitle())
        .contents(review.getContents())
        .updatedAt(review.getUpdatedAt())
        .user(UserConverter.toUserSimpleInfo(review.getUser()))
        .movie(MovieConverter.toMovieCardInfo(review.getMovie(), rating))
        .build();
  }

  public static ReviewSimpleResponse toReviewSimpleResponse(Review review) {
    return ReviewSimpleResponse.builder()
        .id(review.getId())
        .title(review.getTitle())
        .contents(review.getContents())
        .updatedAt(review.getUpdatedAt())
        .user(UserConverter.toUserSimpleInfo(review.getUser()))
        .build();
  }

  public static UserReviewResponse toUserReviewResponse(Review review) {
    return UserReviewResponse.builder()
        .id(review.getId())
        .title(review.getTitle())
        .updatedAt(review.getUpdatedAt())
        .likeCount(review.getLikeCount())
        .movie(MovieConverter.toMovieSimpleInfo(review.getMovie()))
        .build();
  }
}

package org.movie.reviewer.domain.review.dto;

import java.util.List;
import org.movie.reviewer.domain.movie.dto.response.MovieCardInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewSimpleResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
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

  public static ReviewDetailResponse toReviewDetailResponse(ReviewDetailInfo info, Double rating) {
    return ReviewDetailResponse.builder()
        .id(info.getReviewId())
        .title(info.getReviewTitle())
        .contents(info.getContents())
        .updatedAt(info.getUpdatedAt())
        .user(
            UserSimpleInfo.builder()
                .id(info.getUserId())
                .nickname(info.getNickname())
                .profileImage(info.getProfileImage())
                .build())
        .movie(
            MovieCardInfo.builder()
                .id(info.getMovieId())
                .title(info.getMovieTitle())
                .movieImage(info.getMovieImage())
                .genre(info.getMovieGenre())
                .country(info.getCountry())
                .runningTime(info.getRunningTime())
                .rating(rating)
                .build())
        .build();
  }

  public static List<ReviewSimpleResponse> toReviewSimpleResponse(List<Review> reviews) {
    return reviews.stream().map(review ->
            ReviewSimpleResponse.builder()
                .id(review.getId())
                .title(review.getTitle())
                .contents(review.getContents())
                .updatedAt(review.getUpdatedAt())
                .user(
                    UserSimpleInfo.builder()
                        .id(review.getUser().getId())
                        .nickname(review.getUser().getNickname())
                        .profileImage(review.getUser().getProfileImage())
                        .build())
                .build())
        .toList();
  }
}

package org.movie.reviewer.domain.review.dto;

import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;
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
}

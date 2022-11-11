package org.movie.reviewer.domain.user.dto;

import java.util.List;
import org.movie.reviewer.domain.rating.dto.response.UserRatingInfo;
import org.movie.reviewer.domain.review.dto.response.UserReviewInfo;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public static UserDetailResponse toUserDetailResponse(User user, List<UserReviewInfo> reviews,
      List<UserRatingInfo> ratings) {
    return UserDetailResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .introduction(user.getIntroduction())
        .profileImage(user.getProfileImage())
        .reviews(reviews)
        .ratings(ratings)
        .build();
  }
}

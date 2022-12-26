package org.movie.reviewer.domain.user.dto;

import java.util.List;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.request.SignUpRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public static UserDetailResponse toUserDetailResponse(User user, List<UserReviewResponse> reviews,
      List<UserRatingResponse> ratings) {
    return UserDetailResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .introduction(user.getIntroduction())
        .profileImage(user.getProfileImage())
        .reviews(reviews)
        .ratings(ratings)
        .build();
  }

  public static UserSimpleInfo toUserSimpleInfo(User user) {
    return UserSimpleInfo.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImage(user.getProfileImage())
        .build();
  }

  public static User toUser(SignUpRequest request) {
    return User.builder()
        .email(request.getEmail())
        .password(request.getPassword())
        .nickname(request.getNickname())
        .build();
  }

  public static CustomUserDetails toUserDetails(User user) {
    return new CustomUserDetails(user);
  }
}

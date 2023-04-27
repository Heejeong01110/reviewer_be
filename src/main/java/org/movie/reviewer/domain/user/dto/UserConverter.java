package org.movie.reviewer.domain.user.dto;

import java.util.List;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
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
        .authority(UserRole.ROLE_MEMBER)
        .build();
  }

  public static User toEmailUpdatedUser(User user, String email) {
    return User.builder()
        .id(user.getId())
        .email(email)
        .password(user.getPassword())
        .nickname(user.getNickname())
        .introduction(user.getIntroduction())
        .profileImage(user.getProfileImage())
        .authority(user.getAuthority())
        .build();
  }

  public static User toNicknameUpdatedUser(User user, String nickname) {
    return User.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .nickname(nickname)
        .introduction(user.getIntroduction())
        .profileImage(user.getProfileImage())
        .authority(user.getAuthority())
        .build();
  }

  public static User toPasswordUpdatedUser(User user, String password) {
    return User.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(password)
        .nickname(user.getNickname())
        .introduction(user.getIntroduction())
        .profileImage(user.getProfileImage())
        .authority(user.getAuthority())
        .build();
  }

  public static CustomUserDetails toUserDetails(User user) {
    return new CustomUserDetails(user);
  }

}

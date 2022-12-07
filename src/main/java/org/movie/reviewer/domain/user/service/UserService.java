package org.movie.reviewer.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.service.ReviewService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.JsonWebTokenDto;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.request.SignUpRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.movie.reviewer.global.security.utils.JsonWebTokenIssuer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final String GRANT_TYPE_BEARER = "Bearer";
  private final JsonWebTokenIssuer jwtIssuer;
  private final UserRepository userRepository;
  private final ReviewService reviewService;
  private final RatingService ratingService;
  private final PasswordEncoder passwordEncoder;

  public UserDetailResponse getUserById(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, userId));

    List<UserReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
    List<UserRatingResponse> ratings = ratingService.getRatingsByUserId(userId);

    return UserConverter.toUserDetailResponse(user, reviews, ratings);
  }

  public boolean checkEmailDuplicate(String email) {
    return !userRepository.existsByEmail(email);
  }

  public boolean checkNicknameDuplicate(String nickname) {
    return !userRepository.existsByNickname(nickname);
  }

  @Transactional
  public User save(SignUpRequest request) {
    if (!checkEmailDuplicate(request.getEmail()) ||
        !checkNicknameDuplicate(request.getNickname())) {
      throw new RuntimeException();
    }

    request.encodePassword(passwordEncoder.encode(request.getPassword()));
    return userRepository.save(UserConverter.toUser(request));
  }

  private String resolveToken(String bearerToken) {
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE_BEARER)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private JsonWebTokenDto createJsonWebTokenDto(User user) {
    String userName = user.getEmail();
    String authority = user.getAuthority().toString();
    return JsonWebTokenDto.builder()
        .grantType(GRANT_TYPE_BEARER)
        .accessToken(jwtIssuer.createAccessToken(userName, authority))
        .refreshToken(jwtIssuer.createRefreshToken(userName, authority))
        .build();
  }

}

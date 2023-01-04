package org.movie.reviewer.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.service.ReviewService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.request.SignUpRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
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

  public boolean isDuplicatedEmail(String email) {
    return !userRepository.existsByEmail(email);
  }

  public boolean isDuplicatedNickname(String nickname) {
    return !userRepository.existsByNickname(nickname);
  }

  @Transactional
  public User save(SignUpRequest request) {
    if (!isDuplicatedEmail(request.getEmail()) ||
        !isDuplicatedNickname(request.getNickname())) {
      throw new RuntimeException("test중 - 아이디 또는 닉네임 중복");
    }

    request.encodePassword(passwordEncoder.encode(request.getPassword()));
    return userRepository.save(UserConverter.toUser(request));
  }


  public boolean checkPasswordValid(User user, String password) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new RuntimeException("test중 - 일치하지 않는 이메일입니다.");
    }
    return true;
  }

  public User updateUserEmail(User user, String email) {
    user.updateEmail(email);
    userRepository.save(user);
    return user;
  }
}

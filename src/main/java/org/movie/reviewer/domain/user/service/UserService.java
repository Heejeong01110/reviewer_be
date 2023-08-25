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
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;
import org.movie.reviewer.domain.user.exception.DuplicateEmailException;
import org.movie.reviewer.domain.user.exception.DuplicateNicknameException;
import org.movie.reviewer.domain.user.exception.PasswordValidFailException;
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

  public boolean isUsableEmail(String email) {
    return !userRepository.existsByEmail(email);
  }

  public boolean isUsableNickname(String nickname) {
    return !userRepository.existsByNickname(nickname);
  }

  @Transactional
  public User save(SignUpRequest request) {
    if(!isUsableEmail(request.getEmail())){
      throw new DuplicateEmailException(ErrorMessage.EMAIL_DUPLICATED, request.getEmail());
    }else if(!isUsableNickname(request.getNickname())){
      throw new DuplicateNicknameException(ErrorMessage.NICKNAME_DUPLICATED, request.getNickname());
    }

    request.encodePassword(passwordEncoder.encode(request.getPassword()));
    return userRepository.save(UserConverter.toUser(request));
  }


  public boolean checkPasswordValid(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new PasswordValidFailException(ErrorMessage.PASSWORD_VALID_FAIL);
    }
    return true;
  }

  @Transactional
  public UserSimpleInfo updateUserEmail(String oldEmail, String newEmail) {
    User user = userRepository.findByEmail(oldEmail)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, oldEmail));
    return UserConverter.toUserSimpleInfo(
        userRepository.save(UserConverter.toEmailUpdatedUser(user, newEmail)));
  }

  @Transactional
  public UserSimpleInfo updateUserNickname(String email, String nickname) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));
    return UserConverter.toUserSimpleInfo(
        userRepository.save(UserConverter.toNicknameUpdatedUser(user, nickname)));
  }

  @Transactional
  public UserSimpleInfo updateUserPassword(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));
    return UserConverter.toUserSimpleInfo(
        userRepository.save(UserConverter.toPasswordUpdatedUser(user, password)));
  }

  public UserSimpleInfo getUserByEmail(String email) {
    return UserConverter.toUserSimpleInfo(userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email)));
  }
}

package org.movie.reviewer.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.service.ReviewService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  private final ReviewService reviewService;

  private final RatingService ratingService;

  public UserDetailResponse getUserById(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, userId));

    List<UserReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
    List<UserRatingResponse> ratings = ratingService.getRatingsByUserId(userId);

    return UserConverter.toUserDetailResponse(user, reviews, ratings);
  }
}

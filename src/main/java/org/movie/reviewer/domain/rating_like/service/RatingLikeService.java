package org.movie.reviewer.domain.rating_like.service;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.repository.RatingRepository;
import org.movie.reviewer.domain.rating_like.domain.RatingLike;
import org.movie.reviewer.domain.rating_like.dto.RatingLikeConverter;
import org.movie.reviewer.domain.rating_like.repository.RatingLikeRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingLikeService {

  private final RatingLikeRepository ratingLikeRepository;
  private final RatingRepository ratingRepository;
  private final UserRepository userRepository;

  @Transactional
  public RatingLike updateRatingLike(String email, Long ratingId, Long likeType) {
    Rating rating = ratingRepository.findById(ratingId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.RATING_NOT_FOUNDED, ratingId));
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));

    RatingLike updatedLike;
    if (ratingLikeRepository.existsByUserIdAndRatingId(user.getId(), ratingId)) {
      RatingLike ratingLike = ratingLikeRepository.getRatingLikeByUserIdAndRatingId(user.getId(), ratingId)
          .orElseThrow(() -> new NotFoundException(ErrorMessage.RATING_LIKE_NOT_FOUNDED, ratingId));
      updatedLike = RatingLikeConverter.toRatingLike(ratingLike, likeType);
    } else {
      updatedLike = RatingLike.builder()
          .likeType(likeType)
          .user(user)
          .rating(rating)
          .build();
    }
    return ratingLikeRepository.save(updatedLike);
  }
}

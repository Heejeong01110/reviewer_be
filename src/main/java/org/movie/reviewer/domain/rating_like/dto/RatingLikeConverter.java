package org.movie.reviewer.domain.rating_like.dto;

import org.movie.reviewer.domain.rating_like.domain.RatingLike;
import org.springframework.stereotype.Component;

@Component
public class RatingLikeConverter {

  public static RatingLike toRatingLike(RatingLike ratingLike, Long type) {
    return RatingLike.builder()
        .id(ratingLike.getId())
        .likeType(type)
        .rating(ratingLike.getRating())
        .user(ratingLike.getUser())
        .rating(ratingLike.getRating())
        .build();
  }
}

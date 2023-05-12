package org.movie.reviewer.domain.rating.dto;

import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.request.RatingCreateRequest;
import org.movie.reviewer.domain.rating.dto.response.RatingResponse;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.springframework.stereotype.Component;

@Component
public class RatingConverter {

  public static RatingResponse toRatingResponse(Rating rating) {
    return RatingResponse.builder()
        .id(rating.getId())
        .contents(rating.getContents())
        .rating(rating.getRating())
        .updatedAt(rating.getUpdatedAt())
        .user(UserConverter.toUserSimpleInfo(rating.getUser()))
        .build();
  }

  public static UserRatingResponse toUserRatingResponse(Rating rating) {
    return UserRatingResponse.builder()
        .id(rating.getId())
        .contents(rating.getContents())
        .updatedAt(rating.getUpdatedAt())
        .likeCount(rating.getLikeCount())
        .movie(MovieConverter.toMovieSimpleInfo(rating.getMovie()))
        .build();
  }

  public static Rating toRating(RatingCreateRequest request, User user, Movie movie) {
    return Rating.builder()
        .contents(request.getContents())
        .rating(request.getRating())
        .user(user)
        .movie(movie)
        .build();
  }
}

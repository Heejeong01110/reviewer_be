package org.movie.reviewer.domain.favorite.dto;

import org.movie.reviewer.domain.favorite.domain.Favorite;
import org.movie.reviewer.domain.favorite.dto.response.UserFavoriteResponse;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.springframework.stereotype.Component;

@Component
public class FavoriteConverter {

  public static UserFavoriteResponse toUserFavoriteResponse(Favorite favorite) {
    return UserFavoriteResponse.builder()
        .id(favorite.getId())
        .movie(MovieConverter.toMovieSimpleInfo(favorite.getMovie()))
        .build();
  }
}

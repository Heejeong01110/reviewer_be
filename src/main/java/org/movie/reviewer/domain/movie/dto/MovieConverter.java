package org.movie.reviewer.domain.movie.dto;

import java.util.List;
import org.movie.reviewer.domain.movie.domain.Actor;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.response.ActorInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieCardInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.springframework.stereotype.Component;

@Component
public class MovieConverter {

  public static MovieDetailResponse toMovieDetailResponse(MovieDetailResponse response,
      List<ActorInfo> actors) {
    return MovieDetailResponse.builder()
        .id(response.getId())
        .title(response.getTitle())
        .movieImage(response.getMovieImage())
        .genre(response.getGenre())
        .rating(response.getRating())
        .country(response.getCountry())
        .runningTime(response.getRunningTime())
        .summary(response.getSummary())
        .director(response.getDirector())
        .actors(actors)
        .build();
  }

  public static MovieTitleResponse toMovieTitleResponse(Movie movie, Double rating) {
    return MovieTitleResponse.builder()
        .id(movie.getId())
        .title(movie.getTitle())
        .movieImage(movie.getMovieImage())
        .rating(rating)
        .build();
  }

  public static ActorInfo toActorInfo(Actor actor) {
    return ActorInfo.builder()
        .id(actor.getId())
        .name(actor.getName())
        .role(actor.getRole())
        .build();
  }

  public static MovieSimpleInfo toMovieSimpleInfo(Movie movie) {
    return MovieSimpleInfo.builder()
        .id(movie.getId())
        .title(movie.getTitle())
        .movieImage(movie.getMovieImage())
        .build();
  }

  public static MovieCardInfo toMovieCardInfo(Movie movie, Double rating) {
    return MovieCardInfo.builder()
        .id(movie.getId())
        .title(movie.getTitle())
        .movieImage(movie.getMovieImage())
        .genre(movie.getGenre())
        .country(movie.getCountry())
        .runningTime(movie.getRunningTime())
        .rating(rating)
        .build();
  }

}

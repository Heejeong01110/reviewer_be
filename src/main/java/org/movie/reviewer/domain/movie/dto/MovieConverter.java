package org.movie.reviewer.domain.movie.dto;

import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.response.MovieResponse;
import org.springframework.stereotype.Component;

@Component
public class MovieConverter {

  public static MovieResponse toMovieResponse(Movie movie) {
    return MovieResponse.builder()
        .id(movie.getId())
        .title(movie.getTitle())
        .director(movie.getDirector())
        .genre(movie.getGenre())
        .country(movie.getCountry())
        .runningTime(movie.getRunningTime())
        .summary(movie.getSummary())
        .movieImage(movie.getMovieImage())
        .build();
  }
}

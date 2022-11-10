package org.movie.reviewer.domain.movie.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.movie.reviewer.domain.movie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class MovieApi {

  private final MovieService movieService;

  @GetMapping("movies")
  public ResponseEntity<List<MovieTitleResponse>> getMovies() {
    return ResponseEntity.ok(movieService.getMovieTitleList());
  }

  @GetMapping("movies/{movieId}")
  public ResponseEntity<MovieDetailResponse> getMovie(@PathVariable("movieId") Long movieId) {
    return ResponseEntity.ok(movieService.getMovieById(movieId));
  }

}

package org.movie.reviewer.domain.rating.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.movie.reviewer.domain.rating.dto.response.RatingResponse;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class RatingApi {

  private final RatingService ratingService;

  @GetMapping("/movies/{movieId}/ratings")
  public ResponseEntity<List<RatingResponse>> getRatingsByMovieId(@PathVariable("movieId") Long movieId) {
    return ResponseEntity.ok(ratingService.getRatingsByMovieId(movieId));
  }

}

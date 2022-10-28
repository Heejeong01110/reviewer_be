package org.movie.reviewer.domain.movie.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.movie.dto.response.MovieResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.movie.reviewer.domain.movie.exception.NotFoundException;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

  private final MovieRepository movieRepository;
  private final RatingService ratingService;

  public List<MovieTitleResponse> getMovieTitleList() {
    return movieRepository.findAll().stream()
        .map(MovieConverter::toMovieTitleResponse)
        .map(movie -> MovieConverter.toMovieTitleResponse(
            movie,
            ratingService.getRatingScoreByMovieId(movie.getId())
        )).toList();
  }

  public MovieResponse getMovieById(Long movieId) {
    return movieRepository.findMovieById(movieId).map(MovieConverter::toMovieResponse)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MOVIE_NOT_FOUNDED, movieId));
  }

}

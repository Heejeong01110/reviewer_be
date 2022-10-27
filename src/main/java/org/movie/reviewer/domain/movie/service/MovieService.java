package org.movie.reviewer.domain.movie.service;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.movie.dto.response.MovieResponse;
import org.movie.reviewer.domain.movie.exception.NotFoundException;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

  private final MovieRepository movieRepository;

  public Page<MovieResponse> getMovieList(Pageable pageable) {
    return movieRepository.findAllBySorting(pageable).map(MovieConverter::toMovieResponse);
  }

  public MovieResponse getMovieById(Long movieId) {
    return movieRepository.findMovieById(movieId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MOVIE_NOT_FOUNDED, movieId));
  }

}

package org.movie.reviewer.domain.movie.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

  private final MovieRepository movieRepository;

  public List<MovieTitleResponse> getMovieTitleList() {
    return movieRepository.findMovieTitleAll();
  }

  public MovieDetailResponse getMovieById(Long movieId) {
    return MovieConverter.toMovieDetailResponse(
        movieRepository.findMovieDetailById(movieId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.MOVIE_NOT_FOUNDED, movieId)),
        movieRepository.findActorsByMovieId(movieId)
            .stream().map(MovieConverter::toActorInfo).toList());
  }

}

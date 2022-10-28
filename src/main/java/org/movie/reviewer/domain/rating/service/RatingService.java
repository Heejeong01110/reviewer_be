package org.movie.reviewer.domain.rating.service;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

  private final RatingRepository ratingRepository;

  public Double getRatingScoreByMovieId(Long movieId) {
    return ratingRepository.getRatingAvgByMovieId(movieId);
  }

}

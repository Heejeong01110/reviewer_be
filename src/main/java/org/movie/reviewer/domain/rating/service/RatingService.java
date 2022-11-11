package org.movie.reviewer.domain.rating.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.dto.RatingConverter;
import org.movie.reviewer.domain.rating.dto.response.RatingResponse;
import org.movie.reviewer.domain.rating.dto.response.UserRatingInfo;
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

  public List<RatingResponse> getRatingsByMovieId(Long movieId) {
    return ratingRepository.getRatingsByMovieId(movieId)
        .stream().map(RatingConverter::toRatingResponse).toList();
  }

  public List<UserRatingInfo> getRatingsByUserId(Long userId) {
    return ratingRepository.getRatingsByUserId(userId)
        .stream().map(RatingConverter::toUserRatingInfo).toList();
  }

}

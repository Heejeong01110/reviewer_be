package org.movie.reviewer.domain.rating.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.RatingConverter;
import org.movie.reviewer.domain.rating.dto.request.RatingCreateRequest;
import org.movie.reviewer.domain.rating.dto.response.RatingResponse;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.rating.repository.RatingRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

  private final RatingRepository ratingRepository;
  private final UserRepository userRepository;
  private final MovieRepository movieRepository;

  public Double getRatingScoreByMovieId(Long movieId) {
    return ratingRepository.getRatingAvgByMovieId(movieId);
  }

  public List<RatingResponse> getRatingsByMovieId(Long movieId) {
    return ratingRepository.getRatingsByMovieId(movieId)
        .stream().map(RatingConverter::toRatingResponse).toList();
  }

  public List<UserRatingResponse> getRatingsByUserId(Long userId) {
    return ratingRepository.getRatingsByUserId(userId)
        .stream().map(RatingConverter::toUserRatingResponse).toList();
  }

  public Rating createRating(String email, Long movieId, RatingCreateRequest request) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.MOVIE_NOT_FOUNDED, movieId));
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));
    return ratingRepository.save(RatingConverter.toRating(request, user, movie));
  }
}

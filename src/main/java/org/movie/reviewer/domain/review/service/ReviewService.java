package org.movie.reviewer.domain.review.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.request.ReviewCreateRequest;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewSimpleResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.repository.ReviewRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final MovieRepository movieRepository;
  private final UserRepository userRepository;

  private final RatingService ratingService;

  public List<ReviewTitleResponse> getReviewTitleList() {
    return reviewRepository.findReviewTitleAll().stream()
        .map(ReviewConverter::toReviewTitleResponse).toList();
  }

  public ReviewDetailResponse getReviewById(Long reviewId) {
    Review review = reviewRepository.findReviewDetailById(reviewId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.REVIEW_NOT_FOUNDED, reviewId));
    return ReviewConverter.toReviewDetailResponse(review,
        ratingService.getRatingScoreByMovieId(review.getMovie().getId()));
  }

  public List<ReviewSimpleResponse> getSimpleReviewsByMovieId(Long movieId) {
    return reviewRepository.findReviewsByMovieId(movieId)
        .stream().map(ReviewConverter::toReviewSimpleResponse).toList();
  }

  public List<UserReviewResponse> getReviewsByUserId(Long userId) {
    return reviewRepository.findReviewsByUserId(userId)
        .stream().map(ReviewConverter::toUserReviewResponse).toList();
  }

  //todo 동명의 영화를 검색해서 movieId를 받을 수 있게 프론트엔드 쪽 작업 필요
  @Transactional
  public Review createReview(String email, ReviewCreateRequest request) {
    Movie movie = movieRepository.findById(request.getMovieId())
        .orElseThrow(
            () -> new NotFoundException(ErrorMessage.MOVIE_NOT_FOUNDED, request.getMovieId()));
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));
    return reviewRepository.save(ReviewConverter.toReview(request, movie, user));
  }

}

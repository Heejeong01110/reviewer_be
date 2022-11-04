package org.movie.reviewer.domain.review.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.repository.ReviewRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final RatingService ratingService;

  public List<ReviewTitleResponse> getReviewTitleList() {
    return reviewRepository.findReviewTitleAll().stream()
        .map(ReviewConverter::toReviewTitleResponse).toList();
  }

  public ReviewDetailResponse getReviewById(Long reviewId) {
    ReviewDetailInfo reviewDetailInfo = reviewRepository.findReviewDetailById(reviewId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.REVIEW_NOT_FOUNDED, reviewId));
    return ReviewConverter.toReviewDetailResponse(
        reviewDetailInfo, ratingService.getRatingScoreByMovieId(reviewDetailInfo.getMovieId()));
  }
}

package org.movie.reviewer.domain.review.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class reviewApi {

  private final ReviewService reviewService;

  @GetMapping("reviews")
  public ResponseEntity<List<ReviewTitleResponse>> getReviews() {
    return ResponseEntity.ok(reviewService.getReviewTitleList());
  }

  @GetMapping("reviews/{reviewId}")
  public ResponseEntity<ReviewDetailResponse> getReview(@PathVariable("reviewId") Long reviewId) {
    return ResponseEntity.ok(reviewService.getReviewById(reviewId));
  }

}

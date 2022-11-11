package org.movie.reviewer.domain.review_comment.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.dto.response.UserCommentResponse;
import org.movie.reviewer.domain.review_comment.service.ReviewCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ReviewCommentApi {

  private final ReviewCommentService reviewCommentService;

  @GetMapping("reviews/{reviewId}/comments")
  public ResponseEntity<List<ReviewCommentResponse>> getReviewComments(
      @PathVariable("reviewId") Long reviewId) {
    return ResponseEntity.ok(reviewCommentService.getReviewComments(reviewId));
  }

  @GetMapping("/account/{userId}/comments")
  public ResponseEntity<List<UserCommentResponse>> getReviewsByUserId(
      @PathVariable("userId") Long userId) {
    return ResponseEntity.ok(reviewCommentService.getCommentsByUserId(userId));
  }

}

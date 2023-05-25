package org.movie.reviewer.domain.review_comment.api;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.dto.response.UserCommentResponse;
import org.movie.reviewer.domain.review_comment.service.ReviewCommentService;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("accounts/{userId}/comments")
  public ResponseEntity<List<UserCommentResponse>> getReviewsByUserId(
      @PathVariable("userId") Long userId) {
    return ResponseEntity.ok(reviewCommentService.getCommentsByUserId(userId));
  }

  @PostMapping("reviews/{reviewId}/comments")
  public ResponseEntity<Void> createReview(
      @RequestBody Map<String, String> request,
      @PathVariable("reviewId") Long reviewId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    reviewCommentService.createReviewComment(
        reviewId, userDetails.getEmail(), request.get("contents"));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}

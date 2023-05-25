package org.movie.reviewer.domain.review_comment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.repository.ReviewRepository;
import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.movie.reviewer.domain.review_comment.dto.ReviewCommentConverter;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.dto.response.UserCommentResponse;
import org.movie.reviewer.domain.review_comment.repository.ReviewCommentRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommentService {

  private final ReviewCommentRepository reviewCommentRepository;
  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;

  public List<ReviewCommentResponse> getReviewComments(Long reviewId) {
    return reviewCommentRepository.findCommentsByReviewId(reviewId)
        .stream().map(ReviewCommentConverter::toReviewCommentResponse).toList();
  }

  public List<UserCommentResponse> getCommentsByUserId(Long userId) {
    return reviewCommentRepository.findCommentsByUserId(userId)
        .stream().map(ReviewCommentConverter::toUserCommentResponse).toList();
  }
  
  @Transactional
  public ReviewComment createReviewComment(Long reviewId, String email, String contents) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.REVIEW_NOT_FOUND, reviewId));
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, email));
    return reviewCommentRepository.save(
        ReviewCommentConverter.toReviewComment(review, user, contents));
  }
}

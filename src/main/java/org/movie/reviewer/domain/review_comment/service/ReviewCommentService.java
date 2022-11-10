package org.movie.reviewer.domain.review_comment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.review_comment.dto.ReviewCommentConverter;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.repository.ReviewCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommentService {

  private final ReviewCommentRepository reviewCommentRepository;

  public List<ReviewCommentResponse> getReviewComments(Long reviewId) {
    return reviewCommentRepository.findReviewCommentsByReviewId(reviewId)
        .stream().map(ReviewCommentConverter::toReviewCommentResponse).toList();
  }
  
}
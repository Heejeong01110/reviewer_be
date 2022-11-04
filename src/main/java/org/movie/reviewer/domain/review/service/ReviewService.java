package org.movie.reviewer.domain.review.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private final ReviewRepository reviewRepository;

  public List<ReviewTitleResponse> getReviewTitleList() {
    return reviewRepository.findReviewTitleAll().stream().map(ReviewConverter::toReviewTitleResponse).toList();
  }
}

package org.movie.reviewer.domain.review_like.service;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.review_like.repository.ReviewLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeService {

  private final ReviewLikeRepository reviewLikeRepository;

}

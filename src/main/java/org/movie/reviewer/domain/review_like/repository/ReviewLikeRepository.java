package org.movie.reviewer.domain.review_like.repository;

import org.movie.reviewer.domain.review_like.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

}

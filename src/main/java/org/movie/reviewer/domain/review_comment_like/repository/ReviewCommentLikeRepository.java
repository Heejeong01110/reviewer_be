package org.movie.reviewer.domain.review_comment_like.repository;

import org.movie.reviewer.domain.review_comment_like.domain.ReviewCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentLikeRepository extends JpaRepository<ReviewCommentLike, Long> {

}

package org.movie.reviewer.domain.review_comment.repository;

import java.util.List;
import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

  @Query("SELECT DISTINCT c FROM ReviewComment c join fetch c.user where c.review.id = :id")
  List<ReviewComment> findReviewCommentsByReviewId(@Param("id") Long reviewId);

}

package org.movie.reviewer.domain.review_comment.repository;

import java.util.List;
import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

  @Query("SELECT DISTINCT c FROM ReviewComment c "
      + "JOIN FETCH c.user "
      + "WHERE c.review.id = :id")
  List<ReviewComment> findCommentsByReviewId(@Param("id") Long reviewId);

  @Query("SELECT DISTINCT c FROM ReviewComment c "
      + "LEFT JOIN FETCH c.review "
      + "WHERE c.user.id = :id")
  List<ReviewComment> findCommentsByUserId(@Param("id") Long userId);
}

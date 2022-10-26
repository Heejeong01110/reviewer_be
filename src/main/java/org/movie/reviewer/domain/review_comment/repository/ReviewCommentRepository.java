package org.movie.reviewer.domain.review_comment.repository;

import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

}

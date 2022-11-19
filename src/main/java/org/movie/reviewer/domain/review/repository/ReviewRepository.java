package org.movie.reviewer.domain.review.repository;

import java.util.List;
import java.util.Optional;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query("SELECT "
      + "new org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo "
      + "(r.id, r.title, count(c.id), u.id, u.nickname, u.profileImage, m.id, m.title, m.movieImage) FROM Review r "
      + "LEFT JOIN ReviewComment c ON r.id = c.review.id "
      + "LEFT JOIN User u ON u.id = r.user.id "
      + "LEFT JOIN Movie m ON m.id = r.movie.id "
      + "GROUP BY r.id"
  )
  List<ReviewTitleInfo> findReviewTitleAll();

  @Query("SELECT DISTINCT r FROM Review r "
      + "LEFT JOIN FETCH r.user "
      + "LEFT JOIN FETCH r.movie "
      + "WHERE r.id = :id"
  )
  Optional<Review> findReviewDetailById(@Param("id") Long reviewId);

  List<Review> findReviewsByMovieId(Long movieId);

  @Query("SELECT DISTINCT r FROM Review r "
      + "LEFT JOIN FETCH r.movie "
      + "WHERE r.user.id = :id")
  List<Review> findReviewsByUserId(@Param("id") Long userId);
}

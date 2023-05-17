package org.movie.reviewer.domain.rating_like.repository;

import java.util.Optional;
import org.movie.reviewer.domain.rating_like.domain.RatingLike;
import org.movie.reviewer.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingLikeRepository extends JpaRepository<RatingLike, Long> {

  @Query("SELECT "
      + "rl FROM RatingLike rl "
      + "WHERE rl.user.id = :userId "
      + "AND rl.rating.id = :ratingId"
  )
  Optional<RatingLike> getRatingLikeByUserIdAndRatingId(Long userId, Long ratingId);

  boolean existsByUserIdAndRatingId(Long userId, Long ratingId);

}

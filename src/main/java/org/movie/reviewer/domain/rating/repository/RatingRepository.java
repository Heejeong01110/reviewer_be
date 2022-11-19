package org.movie.reviewer.domain.rating.repository;

import java.util.List;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

  @Query("SELECT "
      + "avg(r.rating) FROM Movie m "
      + "LEFT JOIN Rating r ON m.id = r.movie.id "
      + "GROUP BY m.id "
      + "HAVING m.id = :movieId"
  )
  Double getRatingAvgByMovieId(Long movieId);

  @Query("SELECT DISTINCT r FROM Rating r "
      + "JOIN FETCH r.user "
      + "WHERE r.movie.id = :id")
  List<Rating> getRatingsByMovieId(@Param("id") Long movieId);

  @Query("SELECT DISTINCT r FROM Rating r "
      + "JOIN FETCH r.movie "
      + "WHERE r.user.id = :id")
  List<Rating> getRatingsByUserId(@Param("id") Long userId);

}

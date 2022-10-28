package org.movie.reviewer.domain.rating.repository;

import org.movie.reviewer.domain.rating.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

  @Query("SELECT "
      + "avg(r.rating) FROM Movie m "
      + "LEFT JOIN Rating r ON m.id = r.movie.id "
      + "GROUP BY m.id "
      + "HAVING m.id = :movieId"
  )
  Float getRatingAvgByMovieId(Long movieId);

}

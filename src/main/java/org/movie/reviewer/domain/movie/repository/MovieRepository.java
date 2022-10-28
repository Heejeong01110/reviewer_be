package org.movie.reviewer.domain.movie.repository;

import java.util.Optional;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  Optional<Movie> findMovieById(@Param("id") Long movieId);

}

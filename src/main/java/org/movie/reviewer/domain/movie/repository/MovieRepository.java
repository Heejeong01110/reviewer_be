package org.movie.reviewer.domain.movie.repository;

import java.util.Optional;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.response.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  Optional<MovieResponse> findMovieById(@Param("id") Long movieId);

  Page<Movie> findAllBySorting(Pageable pageable);

}

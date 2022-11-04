package org.movie.reviewer.domain.movie.repository;

import java.util.List;
import java.util.Optional;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.response.ActorInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  @Query("SELECT "
      + "new org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse "
      + "(m.id, m.title, m.movieImage, m.genre, m.country, m.runningTime,  m.summary, m.director, avg(r.rating)) FROM Movie m "
      + "LEFT JOIN Rating r ON m.id = r.movie.id "
      + "WHERE m.id = :id "
  )
  Optional<MovieDetailResponse> findMovieDetailById(@Param("id") Long movieId);

  @Query("SELECT "
      + "new org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse "
      + "(m.id, m.title, m.movieImage, avg(r.rating)) FROM Movie m "
      + "LEFT JOIN Rating r ON m.id = r.movie.id "
      + "GROUP BY m.id"
  )
  List<MovieTitleResponse> findMovieTitleAll();


  @Query("SELECT "
      + "new org.movie.reviewer.domain.movie.dto.response.ActorInfo "
      + "(a.id, a.name, a.role) FROM Actor a "
      + "WHERE a.movie.id = :id "
  )
  List<ActorInfo> findActorsByMovieId(@Param("id") Long movieId);

}

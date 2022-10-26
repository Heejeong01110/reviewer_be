package org.movie.reviewer.domain.rating.repository;

import org.movie.reviewer.domain.rating.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

}

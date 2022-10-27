package org.movie.reviewer.domain.rating_like.repository;

import org.movie.reviewer.domain.rating_like.domain.RatingLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingLikeRepository extends JpaRepository<RatingLike, Long> {

}

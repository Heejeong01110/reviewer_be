package org.movie.reviewer.domain.favorite.repository;

import org.movie.reviewer.domain.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}

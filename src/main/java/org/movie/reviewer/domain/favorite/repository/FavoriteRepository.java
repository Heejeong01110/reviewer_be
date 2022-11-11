package org.movie.reviewer.domain.favorite.repository;

import java.util.List;
import org.movie.reviewer.domain.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  List<Favorite> findFavoritesByUserId(Long userId);
}

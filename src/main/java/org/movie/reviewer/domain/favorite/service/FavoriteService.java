package org.movie.reviewer.domain.favorite.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.favorite.dto.FavoriteConverter;
import org.movie.reviewer.domain.favorite.dto.response.UserFavoriteResponse;
import org.movie.reviewer.domain.favorite.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;

  public List<UserFavoriteResponse> getFavoritesByUserId(Long userId) {
    return favoriteRepository.findFavoritesByUserId(userId)
        .stream().map(FavoriteConverter::toUserFavoriteResponse).toList();
  }

}

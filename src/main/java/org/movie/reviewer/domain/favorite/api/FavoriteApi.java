package org.movie.reviewer.domain.favorite.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.favorite.dto.response.UserFavoriteResponse;
import org.movie.reviewer.domain.favorite.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class FavoriteApi {

  private final FavoriteService favoriteService;

  @GetMapping("/account/{userId}/favorites")
  public ResponseEntity<List<UserFavoriteResponse>> getReviewsByUserId(
      @PathVariable("userId") Long userId) {
    return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
  }

}

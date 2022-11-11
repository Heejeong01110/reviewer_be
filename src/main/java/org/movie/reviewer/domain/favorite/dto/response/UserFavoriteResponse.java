package org.movie.reviewer.domain.favorite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteResponse {

  private Long id;

  private MovieSimpleInfo movie;

}

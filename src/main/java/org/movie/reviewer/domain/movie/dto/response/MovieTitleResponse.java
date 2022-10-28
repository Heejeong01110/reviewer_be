package org.movie.reviewer.domain.movie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieTitleResponse {

  private Long id;

  private String title;

  private String movieImage;

  private Double rating;

}
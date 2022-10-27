package org.movie.reviewer.domain.movie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

  private Long id;

  private String title;

  private String genre;

  private String country;

  private Long runningTime;

  private String movieImage;

  private String director;

  private String summary;

}

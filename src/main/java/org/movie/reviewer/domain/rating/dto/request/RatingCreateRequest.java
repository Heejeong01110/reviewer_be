package org.movie.reviewer.domain.rating.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingCreateRequest {

  private String contents;

  private Double rating;

}

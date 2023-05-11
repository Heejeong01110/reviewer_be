package org.movie.reviewer.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateRequest {

  private String title;

  private String contents;

  private Long movieId;

}

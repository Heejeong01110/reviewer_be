package org.movie.reviewer.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewTitleResponse {

  private Long id;

  private String title;

  private Long commentCount;

  private UserSimpleInfo user;

  private MovieSimpleInfo movie;

}

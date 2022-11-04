package org.movie.reviewer.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewTitleInfo {

  private Long reviewId;

  private String reviewTitle;

  private Long commentCount;

  private Long userId;

  private String nickname;

  private String profileImage;

  private Long movieId;

  private String movieTitle;

  private String movieImage;

}

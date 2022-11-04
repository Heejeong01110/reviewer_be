package org.movie.reviewer.domain.review.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailInfo {

  private Long reviewId;

  private String reviewTitle;

  private String contents;

  private LocalDateTime updatedAt;

  private Long userId;

  private String nickname;

  private String profileImage;

  private Long movieId;

  private String movieTitle;

  private String movieImage;

  private String movieGenre;

  private String country;

  private Long runningTime;

}

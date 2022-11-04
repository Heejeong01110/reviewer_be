package org.movie.reviewer.domain.review.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.movie.dto.response.MovieCardInfo;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailResponse {

  private Long id;

  private String title;

  private String contents;

  private LocalDateTime updatedAt;

  private UserSimpleInfo user;

  private MovieCardInfo movie;

}

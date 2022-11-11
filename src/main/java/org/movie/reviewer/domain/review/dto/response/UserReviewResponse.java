package org.movie.reviewer.domain.review.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewResponse {

  private Long id;

  private String title;

  private LocalDateTime updatedAt;

  private Long likeCount;

  private MovieSimpleInfo movie;

}

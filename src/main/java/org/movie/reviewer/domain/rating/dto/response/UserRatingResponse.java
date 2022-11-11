package org.movie.reviewer.domain.rating.dto.response;

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
public class
UserRatingResponse {

  private Long id;

  private String contents;

  private LocalDateTime updatedAt;

  private Long likeCount;

  private MovieSimpleInfo movie;

}

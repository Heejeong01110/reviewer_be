package org.movie.reviewer.domain.rating.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

  private Long id;

  private String contents;

  private Double rating;

  private LocalDateTime updatedAt;

  private UserSimpleInfo user;

}

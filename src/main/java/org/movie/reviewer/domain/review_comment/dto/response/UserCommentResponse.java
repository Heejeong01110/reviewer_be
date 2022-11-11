package org.movie.reviewer.domain.review_comment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentResponse {

  private Long id;

  private String contents;

  private Long likeCount;

  private LocalDateTime updatedAt;

  private Long reviewId;

}

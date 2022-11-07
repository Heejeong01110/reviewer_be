package org.movie.reviewer.domain.review_comment.dto.response;

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
public class ReviewCommentResponse {

  private Long id;

  private String contents;

  private Long likeCount;

  private LocalDateTime updatedAt;

  private UserSimpleInfo user;

}

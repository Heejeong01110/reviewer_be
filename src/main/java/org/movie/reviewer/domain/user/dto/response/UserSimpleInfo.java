package org.movie.reviewer.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleInfo {

  private Long id;

  private String nickname;

  private String profileImage;

}

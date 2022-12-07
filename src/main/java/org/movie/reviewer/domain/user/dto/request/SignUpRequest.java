package org.movie.reviewer.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  private String email;

  private String password;

  private String nickname;

  public void encodePassword(String password) {
    this.password = password;
  }
}

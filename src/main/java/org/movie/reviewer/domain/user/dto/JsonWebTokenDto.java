package org.movie.reviewer.domain.user.dto;

import lombok.Builder;

@Builder
public class JsonWebTokenDto {

  private String grantType;

  private String accessToken;

  private String refreshToken;

}

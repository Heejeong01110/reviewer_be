package org.movie.reviewer.domain.auth.controller;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.auth.dto.TokenDto;
import org.movie.reviewer.domain.auth.service.RedisTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class TokenApi {

  public static final String TOKEN_PREFIX = "Bearer ";
  private static final String HEADER_PREFIX = "Authorization";
  private static final String REFRESH_HEADER_PREFIX = "Authorization-refresh";
  private final RedisTokenService redisTokenService;

  @GetMapping("reissue")
  public ResponseEntity<TokenDto> reissue(
      @RequestHeader(value = HEADER_PREFIX) String accessToken,
      @RequestHeader(value = REFRESH_HEADER_PREFIX) String refreshToken,
      HttpServletResponse response) {
    TokenDto tokenDto = getTokenDto(accessToken, refreshToken);

    TokenDto reissue = redisTokenService.reissue(tokenDto);
    response.addHeader(HEADER_PREFIX, TOKEN_PREFIX + reissue.getAccessToken());
    response.addHeader(REFRESH_HEADER_PREFIX, TOKEN_PREFIX + reissue.getRefreshToken());
    return ResponseEntity.ok(reissue);
  }

  @GetMapping("logout")
  public ResponseEntity<Void> logout(
      @RequestHeader(value = HEADER_PREFIX) String accessToken,
      @RequestHeader(value = REFRESH_HEADER_PREFIX) String refreshToken) {
    TokenDto tokenDto = getTokenDto(accessToken, refreshToken);
    redisTokenService.logout(tokenDto);
    return ResponseEntity.ok().build();
  }

  private TokenDto getTokenDto(String accessToken, String refreshToken) {
    return TokenDto.builder()
        .accessToken(resolveToken(accessToken))
        .refreshToken(resolveToken(refreshToken))
        .build();
  }

  private String resolveToken(String accessToken) {
    if (StringUtils.hasText(accessToken) && accessToken.startsWith(TOKEN_PREFIX)) {
      return accessToken.substring(7);
    }
    return null;
  }

}

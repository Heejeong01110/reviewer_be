package org.movie.reviewer.domain.user.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.JsonWebTokenDto;
import org.movie.reviewer.domain.user.dto.request.AuthLoginRequest;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.security.exception.JwtInvalidException;
import org.movie.reviewer.global.security.utils.JsonWebTokenIssuer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final String GRANT_TYPE_BEARER = "Bearer";

  private final UserRepository userRepository;

  private final JsonWebTokenIssuer jwtIssuer;

  private String resolveToken(String bearerToken) {
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE_BEARER)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private JsonWebTokenDto createJsonWebTokenDto(User user) {
    String userName = user.getEmail();
    String authority = user.getAuthority().toString();
    return JsonWebTokenDto.builder()
        .grantType(GRANT_TYPE_BEARER)
        .accessToken(jwtIssuer.createAccessToken(userName, authority))
        .refreshToken(jwtIssuer.createRefreshToken(userName, authority))
        .build();
  }

  public JsonWebTokenDto login(AuthLoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("username is not found"));

    if(user.getPassword().equals(request.getPassword())){
      throw new BadCredentialsException("bad credential: using unmatched password");
    }

    return createJsonWebTokenDto(user);
  }

  public JsonWebTokenDto reissue(String bearerToken) {
    String refreshToken = resolveToken(bearerToken);
    if (!StringUtils.hasText(refreshToken)) {
      throw new JwtInvalidException("invalid grant type");
    }

    Claims claims = jwtIssuer.parseClaimsFromRefreshToken(refreshToken);
    if (claims == null) {
      throw new JwtInvalidException("not exists claims in token");
    }

    User user = userRepository.findByEmail(claims.getSubject())
        .orElseThrow(() -> new UsernameNotFoundException("email is not found"));

    return createJsonWebTokenDto(user);
  }


}

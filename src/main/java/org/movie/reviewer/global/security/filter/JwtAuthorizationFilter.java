package org.movie.reviewer.global.security.filter;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.auth.service.PrincipalDetailsService;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
import org.movie.reviewer.global.security.utils.JsonWebTokenIssuer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

//인가
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final PrincipalDetailsService principalDetailsService;
  private final JsonWebTokenIssuer jwtIssuer;

  //todo 의존성 확인하기
  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String jwt = resolveToken(request);

    if (StringUtils.hasText(jwt)) {
      try {
        Claims claims = jwtIssuer.parseClaimsFromRefreshToken(jwt);
        Authentication jwtAuthenticationToken = new JsonPrincipalAuthenticationToken(jwt);
        Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (AuthenticationException authenticationException) {
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }


  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}

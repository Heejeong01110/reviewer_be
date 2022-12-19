package org.movie.reviewer.global.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.global.security.exception.JwtInvalidException;
import org.movie.reviewer.global.security.provider.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

//인가(?) --> 기본적으로 인가 기능이 필요할 때 거치고, 인증일 때도 토큰 검증을 위해서 거침.
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  public static final String HEADER_PREFIX = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;


  // 헤더에 현재 accept Token에 대한 유효, 만료, 무효 여부를 담아줘야 함
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      String jwt = resolveToken(request, HEADER_PREFIX);
      if (StringUtils.hasText(jwt) && jwtProvider.validAccessToken(jwt)) {
        Authentication token = jwtProvider.getAuthentication(jwt);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.addHeader("AUTH_VALID", "VALID");
      }

    } catch (ExpiredJwtException exception) { //만료
      response.addHeader("AUTH_VALID", "EXPIRED");

      SecurityContextHolder.clearContext();
    } catch (JwtInvalidException exception) { //무효
      response.addHeader("AUTH_VALID", "INVALID");

      SecurityContextHolder.clearContext();
    } catch (AuthenticationException authenticationException) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request, String header) {
    String bearerToken = request.getHeader(header);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}

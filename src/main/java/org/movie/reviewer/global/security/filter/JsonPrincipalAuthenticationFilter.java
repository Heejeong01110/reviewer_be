package org.movie.reviewer.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.movie.reviewer.domain.user.dto.request.LoginRequest;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
import org.movie.reviewer.global.security.utils.JsonWebTokenIssuer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

//인증
@Slf4j
public class JsonPrincipalAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final String GRANT_TYPE_BEARER = "Bearer ";
  private final String HEADER_STRING = "Authorization";
  private final JsonWebTokenIssuer jwtIssuer;

  private final PasswordEncoder passwordEncoder;

  public JsonPrincipalAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
      AuthenticationManager authenticationManager,
      JsonWebTokenIssuer jwtIssuer,
      PasswordEncoder passwordEncoder) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
    this.jwtIssuer = jwtIssuer;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    ObjectMapper om = new ObjectMapper();
    LoginRequest loginRequest = null;
    try {
      loginRequest = om.readValue(request.getInputStream(), LoginRequest.class);

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new AuthenticationServiceException(e.getMessage());
    }

    //로그인 정보를 가지고 임시로 Auth 토큰을 생성해서 인증을 확인한다.
    //detailsService가 호출됨(loadUserByUsername())
    JsonPrincipalAuthenticationToken token =
        new JsonPrincipalAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    token.setDetails(super.authenticationDetailsSource.buildDetails(request));

    //UserDetailsService 확인
    Authentication authenticate = super.getAuthenticationManager().authenticate(token);
    if (!this.passwordEncoder.matches(loginRequest.getPassword(), authenticate.getCredentials().toString())) {
      throw new BadCredentialsException("AbstractUserDetailsAuthenticationProvider.badCredentials");
    }

    return authenticate;
  }

  //JWT 토큰을 생성해서 response에 담아주기
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String accessToken = jwtIssuer.createAccessToken(authResult);
    response.addHeader(HEADER_STRING, GRANT_TYPE_BEARER + accessToken);
    super.successfulAuthentication(request, response, chain, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    log.debug("login fail");
    super.unsuccessfulAuthentication(request, response, failed);
  }
}

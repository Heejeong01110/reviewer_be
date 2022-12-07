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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

//인증
@Slf4j
public class JsonPrincipalAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final String GRANT_TYPE_BEARER = "Bearer ";
  private final String HEADER_STRING = "Authorization";
  private final JsonWebTokenIssuer jwtIssuer;

  public JsonPrincipalAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
      AuthenticationManager authenticationManager,
      JsonWebTokenIssuer jwtIssuer) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
    this.jwtIssuer = jwtIssuer;
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
    //HttpSerlvetRequest의 Holder로 감싸서 Token에도 request에 대한 정보를 가지고 있도록 저장하는 부분이다.
    token.setDetails(super.authenticationDetailsSource.buildDetails(request));

    Authentication authenticate = super.getAuthenticationManager().authenticate(token);

    log.debug("test authentication email : " + authenticate.getPrincipal());
    log.debug("test authentication password : " + authenticate.getCredentials());
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

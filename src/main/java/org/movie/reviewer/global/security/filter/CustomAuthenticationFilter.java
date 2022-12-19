package org.movie.reviewer.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.movie.reviewer.domain.user.dto.request.LoginRequest;
import org.movie.reviewer.global.security.provider.JwtProvider;
import org.movie.reviewer.global.security.provider.RedisTokenProvider;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
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
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final String TOKEN_PREFIX = "Bearer ";
  private static final String HEADER_PREFIX = "Authorization";
  private static final String REFRESH_HEADER_PREFIX = "Authorization-refresh";

  private final JwtProvider jwtProvider;
  private final RedisTokenProvider redisTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public CustomAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
      AuthenticationManager authenticationManager,
      JwtProvider jwtProvider,
      PasswordEncoder passwordEncoder,
      RedisTokenProvider redisTokenProvider) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
    this.redisTokenProvider = redisTokenProvider;
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

    String authority = authResult.getAuthorities().stream().toList().get(0).toString();
    String email = (String) authResult.getPrincipal();

    String accessToken = jwtProvider.createAccessToken(email, authority);
    response.addHeader(HEADER_PREFIX, TOKEN_PREFIX + accessToken);

    String refreshToken = jwtProvider.createRefreshToken(email);
    redisTokenProvider.saveRefreshToken(email, refreshToken);
    response.addHeader(REFRESH_HEADER_PREFIX, TOKEN_PREFIX + refreshToken);

    setSuccessResponse(response, "success");
    super.successfulAuthentication(request, response, chain, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException { //적용 안됨, 일반 에러 메시지 출력
    String failMessage = failed.getMessage();
    setFailResponse(response, failMessage);
    super.unsuccessfulAuthentication(request, response, failed);
  }

  private void setSuccessResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("success", true);
    jsonObject.put("code", 1);
    jsonObject.put("message", message);

    response.getWriter().print(jsonObject);
  }

  private void setFailResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("success", false);
    jsonObject.put("code", -1);
    jsonObject.put("message", message);

    response.getWriter().print(jsonObject);
  }
}

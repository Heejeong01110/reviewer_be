package org.movie.reviewer.global.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.global.security.utils.JsonWebTokenIssuer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtSuccessHandler implements AuthenticationSuccessHandler {

  private final JsonWebTokenIssuer jsonWebTokenIssuer;
  private RequestCache requestCache = new HttpSessionRequestCache();


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    try( PrintWriter writer = response.getWriter()) {

      String accessToken = jsonWebTokenIssuer.createAccessToken(authentication);

      response.setStatus(HttpStatus.ACCEPTED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

      writer.write("{\n\"accessToken\": \""+accessToken+"\"\n}");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

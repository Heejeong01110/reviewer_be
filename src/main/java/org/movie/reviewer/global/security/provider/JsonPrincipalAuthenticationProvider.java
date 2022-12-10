package org.movie.reviewer.global.security.provider;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.auth.service.PrincipalDetailsService;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonPrincipalAuthenticationProvider implements AuthenticationProvider {

  private final PrincipalDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userId = String.valueOf(authentication.getPrincipal());
    UserDetails userDetails = userDetailsService.loadUserByUsername(userId); //id check

    JsonPrincipalAuthenticationToken certifiedToken = new JsonPrincipalAuthenticationToken(
        userDetails.getUsername(),
        userDetails.getPassword(), userDetails.getAuthorities());
    certifiedToken.setDetails(authentication.getDetails());

    return certifiedToken;
  }

  // authentication token 설정 메서드
  @Override
  public boolean supports(Class<?> authentication) {
    return JsonPrincipalAuthenticationToken.class.isAssignableFrom(authentication);
  }

}

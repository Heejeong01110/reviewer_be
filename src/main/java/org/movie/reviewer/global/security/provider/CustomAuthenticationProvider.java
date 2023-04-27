package org.movie.reviewer.global.security.provider;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.service.CustomUserDetailsService;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = String.valueOf(authentication.getPrincipal());
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    JsonPrincipalAuthenticationToken certifiedToken = new JsonPrincipalAuthenticationToken(
        userDetails.getUsername(),
        userDetails.getPassword(),
        userDetails.getAuthorities());
    certifiedToken.setDetails(userDetails);

    return certifiedToken;
  }

  // authentication token 설정 메서드
  @Override
  public boolean supports(Class<?> authentication) {
    return JsonPrincipalAuthenticationToken.class.isAssignableFrom(authentication);
  }

}

package org.movie.reviewer.global.security.provider;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.movie.reviewer.domain.auth.service.PrincipalDetailsService;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JsonPrincipalAuthenticationProvider implements AuthenticationProvider {

  private final String KEY_ROLES = "roles";
  private final byte[] secretKeyByte;

  private final PrincipalDetailsService userDetailsService;
//  private final PasswordEncoder passwordEncoder;

  public JsonPrincipalAuthenticationProvider(@Value("${jwt.secret}") String secretKey,
      PrincipalDetailsService userDetailsService
//      PasswordEncoder passwordEncoder
  ) {
    this.secretKeyByte = secretKey.getBytes();
    this.userDetailsService = userDetailsService;
//    this.passwordEncoder = passwordEncoder;
  }

  private Collection<? extends GrantedAuthority> createGrantedAuthorities(Claims claims) {
    List<String> roles = (List) claims.get(KEY_ROLES);
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    for (String role : roles) {
      grantedAuthorities.add(() -> role);
    }
    return grantedAuthorities;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userId = String.valueOf(authentication.getPrincipal());
    UserDetails userDetails = userDetailsService.loadUserByUsername(userId); //id check

    if (!authentication.getCredentials().toString().equals(userDetails.getPassword())) { //pw check
      throw new BadCredentialsException("AbstractUserDetailsAuthenticationProvider.badCredentials");
    }
//    if(!this.passwordEncoder.matches(authentication.getCredentials().toString(),userDetails.getPassword())){
//      throw new BadCredentialsException("AbstractUserDetailsAuthenticationProvider.badCredentials");
//    }

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

package org.movie.reviewer.global.security.tokens;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private String jsonWebToken;
  private Object principal;
  private Object credentials;

  public JwtAuthenticationToken(String jsonWebToken) {
    super(null);
    this.jsonWebToken = jsonWebToken;
    this.setAuthenticated(false);
  }


  public JwtAuthenticationToken(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  public String getJsonWebToken() {
    return jsonWebToken;
  }
}

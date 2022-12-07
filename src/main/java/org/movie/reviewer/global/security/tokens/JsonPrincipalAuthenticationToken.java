package org.movie.reviewer.global.security.tokens;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class JsonPrincipalAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
  private String jsonWebToken;
  private Object principal; //id
  private Object credentials; //pw

  public JsonPrincipalAuthenticationToken(String jsonWebToken) {
    super(null);
    this.jsonWebToken = jsonWebToken;
    this.setAuthenticated(false);
  }

  public JsonPrincipalAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  public JsonPrincipalAuthenticationToken(Object principal, Object credentials,
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

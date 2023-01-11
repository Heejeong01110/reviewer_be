package org.movie.reviewer.global.security.factory;

import java.util.List;
import org.movie.reviewer.global.security.annotation.WithMockCustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public final class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

    final UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
        annotation.username(),
        annotation.password(),
        AuthorityUtils.createAuthorityList(annotation.roles()));

    securityContext.setAuthentication(authenticationToken);
    return securityContext;
  }
}

package org.movie.reviewer.global.security.factory;

import org.movie.reviewer.global.security.annotation.WithMockCustomAnonymousUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public final class WithAnonymousUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomAnonymousUser> {

  public SecurityContext createSecurityContext(WithMockCustomAnonymousUser withUser) {

    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

    Authentication authentication = new AnonymousAuthenticationToken(
        "key",
        "anonymousUser",
        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    securityContext.setAuthentication(authentication);
    return securityContext;
  }
}

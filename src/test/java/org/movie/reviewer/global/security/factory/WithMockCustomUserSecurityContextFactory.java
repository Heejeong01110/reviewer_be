package org.movie.reviewer.global.security.factory;

import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.global.security.annotation.WithMockCustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public final class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    User user = User.builder()
        .email(annotation.username())
        .password(annotation.password())
        .authority(UserRole.valueOf(annotation.roles()))
        .build();
    UserDetails userDetails = CustomUserDetails.builder().user(user).build();
    final UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        AuthorityUtils.createAuthorityList(annotation.roles()));

    securityContext.setAuthentication(authenticationToken);
    return securityContext;
  }
}

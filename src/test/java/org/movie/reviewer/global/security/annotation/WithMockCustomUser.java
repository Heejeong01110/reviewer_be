package org.movie.reviewer.global.security.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.global.security.factory.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  String username() default "testUser@tgmail.com";

  String password() default "test1234";
  String roles() default "ROLE_MEMBER";
}

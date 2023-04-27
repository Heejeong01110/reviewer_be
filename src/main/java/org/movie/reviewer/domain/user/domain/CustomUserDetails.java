package org.movie.reviewer.domain.user.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import org.movie.reviewer.domain.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private String email;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  @Builder
  public CustomUserDetails(User user) {
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.authorities = Optional.of(List.of(user.getAuthority().name()))
        .orElse(Collections.emptyList())
        .stream().map(SimpleGrantedAuthority::new).toList();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public String getEmail() {
    return this.email;
  }
}

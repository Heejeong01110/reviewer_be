package org.movie.reviewer.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND, username));
    return UserConverter.toUserDetails(user);
  }
}

package org.movie.reviewer.domain.user.api;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserApi {

  private final UserService userService;

  @GetMapping("accounts/{userId}")
  public ResponseEntity<UserDetailResponse> getUserById(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }
}

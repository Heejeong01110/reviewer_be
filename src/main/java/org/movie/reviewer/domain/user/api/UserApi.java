package org.movie.reviewer.domain.user.api;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.dto.JsonWebTokenDto;
import org.movie.reviewer.domain.user.dto.request.AuthLoginRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.service.AuthService;
import org.movie.reviewer.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserApi {

  private static String DUPLICATE_EMAIL_ARGUMENT = "이미 존재하는 이메일 입니다.";
  private static String DUPLICATE_NICKNAME_ARGUMENT = "이미 존재하는 닉네임 입니다.";
  private final String AUTHORIZATION_HEADER = "Authorization";
  private final UserService userService;

  private final AuthService authService;

  @GetMapping("accounts/{userId}")
  public ResponseEntity<UserDetailResponse> getUserById(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @GetMapping("validity_checks/email/{email}")
  public ResponseEntity<String> checkEmailDuplicate(@PathVariable("email") String email) {
    if (userService.checkEmailDuplicate(email)) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATE_EMAIL_ARGUMENT);
  }

  @GetMapping("validity_checks/nickname/{nickname}")
  public ResponseEntity<String> checkNicknameDuplicate(@PathVariable("nickname") String nickname) {
    if (userService.checkNicknameDuplicate(nickname)) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATE_NICKNAME_ARGUMENT);
  }

  @PostMapping("login")
  public JsonWebTokenDto login(AuthLoginRequest userDto) {
    return authService.login(userDto);
  }

  @PostMapping("reissue")
  public JsonWebTokenDto reissue(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
    return authService.reissue(bearerToken);
  }

}

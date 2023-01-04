package org.movie.reviewer.domain.user.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.request.SignUpRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserApi {

  private static String DUPLICATE_EMAIL_ARGUMENT = "이미 존재하는 이메일 입니다.";
  private static String DUPLICATE_NICKNAME_ARGUMENT = "이미 존재하는 닉네임 입니다.";
  private final UserService userService;

  @GetMapping("accounts/{userId}")
  public ResponseEntity<UserDetailResponse> getUserById(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @GetMapping("validity_checks/email/{email}")
  public ResponseEntity<String> checkEmailDuplicate(@PathVariable("email") String email) {
    if (userService.isDuplicatedEmail(email)) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATE_EMAIL_ARGUMENT);
  }

  @GetMapping("validity_checks/nickname/{nickname}")
  public ResponseEntity<String> checkNicknameDuplicate(@PathVariable("nickname") String nickname) {
    if (userService.isDuplicatedNickname(nickname)) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATE_NICKNAME_ARGUMENT);
  }

  @PostMapping("signup")
  public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
    userService.save(request);
    return ResponseEntity.created(linkTo(UserApi.class).slash("login").toUri()).build();
  }

  @PostMapping("validity_checks/password")
  public ResponseEntity<Void> checkPasswordValid(
      @RequestBody String password,
      @AuthenticationPrincipal User user) {
    userService.checkPasswordValid(user, password);
    return ResponseEntity.ok().build();
  }

  @PutMapping("accounts/email")
  public ResponseEntity<Void> updateUserEmail(
      @RequestBody String email,
      @AuthenticationPrincipal User user) {
    userService.updateUserEmail(user, email);
    return ResponseEntity.ok().build();
  }

}

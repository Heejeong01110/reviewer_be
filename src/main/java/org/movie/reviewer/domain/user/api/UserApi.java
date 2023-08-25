package org.movie.reviewer.domain.user.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.user.dto.request.SignUpRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;
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

  @GetMapping("accounts/me")
  public ResponseEntity<UserSimpleInfo> getUserByToken(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(userService.getUserByEmail(userDetails.getEmail()));
  }

  @PostMapping("validity_checks/email")
  public ResponseEntity<String> checkEmailDuplicate(
      @RequestBody Map<String, String> request) {
    if (userService.isUsableEmail(request.get("email"))) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATE_EMAIL_ARGUMENT);
  }

  @PostMapping("validity_checks/nickname")
  public ResponseEntity<String> checkNicknameDuplicate(
      @RequestBody Map<String, String> request) {
    if (userService.isUsableNickname(request.get("nickname"))) {
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
      @RequestBody Map<String, String> request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.checkPasswordValid(userDetails.getEmail(), request.get("password"));
    return ResponseEntity.ok().build();
  }

  @PutMapping("accounts/email")
  public ResponseEntity<UserSimpleInfo> updateUserEmail(
      @RequestBody Map<String, String> request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(
        userService.updateUserEmail(userDetails.getEmail(), request.get("email")));
  }


  @PutMapping("accounts/nickname")
  public ResponseEntity<UserSimpleInfo> updateUserNickname(
      @RequestBody Map<String, String> request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(
        userService.updateUserNickname(userDetails.getEmail(), request.get("nickname")));
  }

  @PutMapping("accounts/password")
  public ResponseEntity<UserSimpleInfo> updateUserPassword(
      @RequestBody Map<String, String> request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(
        userService.updateUserPassword(userDetails.getEmail(), request.get("password")));
  }
}

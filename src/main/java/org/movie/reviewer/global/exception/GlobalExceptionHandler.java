package org.movie.reviewer.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.movie.reviewer.domain.user.exception.DuplicateEmailException;
import org.movie.reviewer.domain.user.exception.DuplicateNicknameException;
import org.movie.reviewer.domain.user.exception.PasswordValidFailException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<String> handleNotFound(NotFoundException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handleDuplicateNickname(DuplicateNicknameException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handlePasswordValidFail(PasswordValidFailException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

}

package org.movie.reviewer.global.security.exception;

public class JwtInvalidException extends RuntimeException {

  public JwtInvalidException(String time_expired) {
    super(time_expired);
  }
}

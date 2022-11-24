package org.movie.reviewer.global.security.exception;

public class JwtInvalidException extends RuntimeException {

  public JwtInvalidException(String msg) {
    super(msg);
  }

  public JwtInvalidException(String msg, Throwable cause) {
    super(msg, cause);
  }

}

package org.movie.reviewer.global.security.exception;

public class ExceptionResponse extends RuntimeException {

  public ExceptionResponse(String msg) {
    super(msg);
  }

  public ExceptionResponse(String msg, Throwable cause) {
    super(msg, cause);
  }

}

package org.movie.reviewer.global.exception;

public enum ErrorMessage {

  MOVIE_NOT_FOUNDED("{0}에 해당하는 영화 정보가 없습니다.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}

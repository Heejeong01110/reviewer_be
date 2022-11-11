package org.movie.reviewer.global.exception;

public enum ErrorMessage {

  REVIEW_NOT_FOUNDED("{0}에 해당하는 리뷰 정보가 없습니다."),
  USER_NOT_FOUND("{0}에 해당하는 사용자 정보가 없습니다."),
  MOVIE_NOT_FOUNDED("{0}에 해당하는 영화 정보가 없습니다.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}

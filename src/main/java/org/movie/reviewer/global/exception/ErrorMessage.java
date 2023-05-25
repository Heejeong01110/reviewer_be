package org.movie.reviewer.global.exception;

public enum ErrorMessage {

  REVIEW_NOT_FOUNDED("{0}에 해당하는 리뷰 정보가 없습니다."),
  USER_NOT_FOUND("{0}에 해당하는 사용자 정보가 없습니다."),
  MOVIE_NOT_FOUNDED("{0}에 해당하는 영화 정보가 없습니다."),
  RATING_NOT_FOUNDED("{0}에 해당하는 평점 정보가 없습니다."),
  RATING_LIKE_NOT_FOUNDED("{0}에 해당하는 평점 좋아요 정보가 없습니다."),
  LIKE_TYPE_NOT_FOUNDED("{0}은 올바르지 않은 요청입니다."),
  REVIEW_NOT_FOUND("{0}에 해당하는 리뷰 정보가 없습니다.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}

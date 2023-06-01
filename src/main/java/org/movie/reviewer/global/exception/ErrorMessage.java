package org.movie.reviewer.global.exception;

public enum ErrorMessage {

  REVIEW_NOT_FOUNDED("{0}에 해당하는 리뷰 정보가 없습니다."),
  USER_NOT_FOUND("{0}에 해당하는 사용자 정보가 없습니다."),
  MOVIE_NOT_FOUNDED("{0}에 해당하는 영화 정보가 없습니다."),
  RATING_NOT_FOUNDED("{0}에 해당하는 평점 정보가 없습니다."),
  RATING_LIKE_NOT_FOUNDED("{0}에 해당하는 평점 좋아요 정보가 없습니다."),
  LIKE_TYPE_NOT_FOUNDED("{0}은 올바르지 않은 요청입니다."),
  REVIEW_NOT_FOUND("{0}에 해당하는 리뷰 정보가 없습니다."),
  EMAIL_DUPLICATED("{0}은 중복되는 이메일 입니다."),
  NICKNAME_DUPLICATED("{0}은 중복되는 닉네임 입니다."),
  PASSWORD_VALID_FAIL("비밀번호가 잘못되었습니다."),
  CRAWLING_NOT_WORKING("영화 정보 크롤링이 비정상 종료되었습니다.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}

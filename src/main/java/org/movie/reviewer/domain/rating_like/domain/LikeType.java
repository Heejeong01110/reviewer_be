package org.movie.reviewer.domain.rating_like.domain;

public enum LikeType {
  UNDEFINE(0L),
  LIKE(1L),
  DISLIKE(2L);

  private final Long num;

  LikeType(long num) {
    this.num = num;
  }

  public Long getNum() {
    return num;
  }
}

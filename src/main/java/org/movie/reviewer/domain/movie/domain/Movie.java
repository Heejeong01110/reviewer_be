package org.movie.reviewer.domain.movie.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "movie")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Movie {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(nullable = false, length = 30)
  private String genre;

  @Column(nullable = false, length = 30)
  private String country;

  @Column(nullable = false)
  private Long runningTime;

  @Column(nullable = false, length = 300)
  private String movieImage;

  @Column(nullable = false, length = 30)
  private String director;

  @Column(length = 20000)
  private String summary;

}
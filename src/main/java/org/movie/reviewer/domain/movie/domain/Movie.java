package org.movie.reviewer.domain.movie.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "movie")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Movie {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String movieCode;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String title;

  @Column(nullable = false, length = 30)
  private String genre;

  @Column(nullable = false, length = 30)
  private String country;

  @Column(nullable = false)
  private Long runningTime;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String movieImage;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String director;

  @Column(nullable = false, columnDefinition = "LONGTEXT")
  private String summary;

}

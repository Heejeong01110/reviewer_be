package org.movie.reviewer.domain.movie.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailResponse {

  private Long id;

  private String title;

  private String movieImage;

  private String genre;

  private String country;

  private Long runningTime;

  private String summary;

  private String director;

  private Double rating;

  private List<ActorInfo> actors;

  public MovieDetailResponse(Long id, String title, String movieImage, String genre,
      String country, Long runningTime, String summary, String director, Double rating) {
    this.id = id;
    this.title = title;
    this.movieImage = movieImage;
    this.genre = genre;
    this.country = country;
    this.runningTime = runningTime;
    this.summary = summary;
    this.director = director;
    this.rating = rating;
  }
}

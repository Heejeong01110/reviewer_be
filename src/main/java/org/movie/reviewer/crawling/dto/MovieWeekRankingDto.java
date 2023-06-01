package org.movie.reviewer.crawling.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieWeekRankingDto {
  private String imgLink;
  private String title;
  private String movieCode;
  private String nation;
  private String genre;
  private String director;
  private String runningTime;
  private String summary;
  private String actor;

}

package org.movie.reviewer.crawling.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.crawling.dto.MovieWeekRankingDto;
import org.movie.reviewer.crawling.service.MovieCrawlingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawling/")
public class MovieCrawlingApi {

  private final MovieCrawlingService movieCrawlingService;


  @GetMapping("movie/weekRanking")
  public ResponseEntity<List<MovieWeekRankingDto>> getMovies() {
    movieCrawlingService.getWeekRankingList();
    return ResponseEntity.ok().build();
  }

}

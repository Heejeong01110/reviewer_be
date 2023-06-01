package org.movie.reviewer.crawling.dto;

import java.util.ArrayList;
import java.util.List;
import org.movie.reviewer.domain.movie.domain.Actor;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieCrawlingConverter {

  public static Movie toMovie(MovieWeekRankingDto dto) {
    return Movie.builder()
        .title(dto.getTitle())
        .genre(dto.getGenre().equals("") ? "" : dto.getGenre().split(", ")[0])
        .country(dto.getNation())
        .movieCode(dto.getMovieCode())
        .runningTime(
            Long.parseLong(dto.getGenre().equals("") ? "0"
                : dto.getRunningTime().substring(7, dto.getRunningTime().length() - 1)))
        .movieImage(dto.getImgLink())
        .director(dto.getDirector())
        .summary(dto.getSummary())
        .build();
  }

  public static List<Actor> toActors(Movie movie, String actorStr) {
    if (actorStr.equals("")) {
      return List.of();
    }

    List<Actor> actorList = new ArrayList<>();
    String person = "", role = "";
    int temp = 0;

    actorStr = actorStr.substring(4, actorStr.length() - 5);
    System.out.println(actorStr);
    for (int i = 0; i < actorStr.length(); i++) {
      char now = actorStr.charAt(i);
      if (now == '(') {
        if (temp != 0) {
          person += now;
        }
        temp++;
      } else if (now == ')') {
        if (temp == 1) {
          System.out.println(person + ", " + role);
          actorList.add(Actor.builder()
              .name(person)
              .role(role)
              .movie(movie).build());
          person = "";
          role = "";
        } else {
          role += now;
        }
        temp--;
      } else if (temp == 0) {
        person += now;
      } else if (temp > 0) {
        role += now;
      }

    }
    return actorList;
  }
}

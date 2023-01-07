package org.movie.reviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@SpringBootApplication
public class ReviewerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReviewerApplication.class, args);
  }

}

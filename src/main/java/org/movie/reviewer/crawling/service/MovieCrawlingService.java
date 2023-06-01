package org.movie.reviewer.crawling.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.crawling.dto.MovieCrawlingConverter;
import org.movie.reviewer.crawling.dto.MovieWeekRankingDto;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.repository.ActorRepository;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieCrawlingService {

  private static final String weekRankingUrl = "http://www.cine21.com/rank/boxoffice/domestic";
  private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
  private static String WEB_DRIVER_PATH = "./chromedriver.exe";

  private final MovieRepository movieRepository;
  private final ActorRepository actorRepository;

  private WebDriver startDrive(String url) {
    System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
    ChromeOptions options = new ChromeOptions().addArguments(
        "--disable-popup-blocking", // 팝업 안띄움
        "headless", // 브라우저 안띄움
        "--disable-gpu", // gpu 비활성화
        "--blink-settings=imagesEnabled=false", // 이미지 다운 안받음
        "--remote-allow-origins=*"); //없으면 접근 안됨
    WebDriver driver = new ChromeDriver(options);
    driver.get(url);
    return driver;
  }

  @Transactional
  public void getWeekRankingList() {
    List<MovieWeekRankingDto> movieDtoList = getWeekRankingDtoList();
    for (MovieWeekRankingDto dto : movieDtoList) {
      Movie movie = movieRepository.save(MovieCrawlingConverter.toMovie(dto));
      System.out.println(dto.getActor());
      actorRepository.saveAll(MovieCrawlingConverter.toActors(movie, dto.getActor()));
    }
  }

  public List<MovieWeekRankingDto> getWeekRankingDtoList() {
    WebDriver driver = startDrive(weekRankingUrl);
    List<MovieWeekRankingDto> movieDtoList = new ArrayList<>();
    try {
      List<WebElement> elements = driver.findElements(By.cssSelector("li.boxoffice_li > a"));
      List<String> detailList = elements.stream().map(item -> item.getAttribute("href")).toList();

      for (int i = 0; i < Math.min(10, detailList.size()); i++) { //갯수 지정
        driver.navigate().to(detailList.get(i));
        WebElement element = driver.findElement(By.cssSelector("div.mov_info"));
        WebElement imgAttr = driver.findElement(By.cssSelector("div.mov_poster > img"));
        movieDtoList.add(
            MovieWeekRankingDto.builder()
                .movieCode(detailList.get(i).split("=")[1])
                .imgLink(imgAttr.getAttribute("src") != null ? imgAttr.getAttribute("src") : "")
                .summary(findElementByCssSelector(driver, "div.story_area > div"))
                .title(findElementByCssSelector(element, "p.tit"))
                .nation(findElementByCssSelector(element, "p:nth-child(3) > span:nth-child(2)"))
                .genre(findElementByCssSelector(element, "p:nth-child(4) > span:nth-child(1)"))
                .director(findElementByCssSelector(element, "p:nth-child(6) > a"))
                .runningTime(
                    findElementByCssSelector(element, "p:nth-child(4) > span:nth-child(2)"))
                .actor(findElementByCssSelector(element, "p:nth-child(7)"))
                .build());
      }
      driver.quit();
    } catch (Exception e) {
      driver.quit();
    }
    return movieDtoList;
  }

  private String findElementByCssSelector(WebDriver element, String cssSelector) {
    try {
      WebElement attribute = element.findElement(By.cssSelector(cssSelector));
      return attribute.getText() != null ? attribute.getText() : "";
    } catch (Exception e) {
      return "";
    }
  }

  private String findElementByCssSelector(WebElement element, String cssSelector) {
    try {
      WebElement attribute = element.findElement(By.cssSelector(cssSelector));
      return attribute.getText() != null ? attribute.getText() : "";
    } catch (Exception e) {
      return "";
    }
  }


}

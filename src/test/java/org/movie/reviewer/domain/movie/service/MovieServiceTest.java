package org.movie.reviewer.domain.movie.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.movie.dto.response.ActorInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.movie.reviewer.domain.movie.exception.NotFoundException;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.domain.rating.service.RatingService;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Spy
  @InjectMocks
  private MovieService movieService;

  @Mock
  private MovieRepository movieRepository;

  @Mock
  private RatingService ratingService;

  private Movie movie1 = Movie.builder()
      .id(0L)
      .country("KR")
      .title("타짜")
      .director("최동훈")
      .movieImage(
          "https://movie-phinf.pstatic.net/20211122_91/1637564743461raGXe_JPEG/movie_image.jpg?type=m203_290_2")
      .genre("CRIME")
      .runningTime(139L)
      .summary("인생을 건 한판 승부 | 큰거 한판에 인생은 예술이 된다! | 목숨을 걸 수 없다면, 배팅하지 마라! | 꽃들의 전쟁")
      .build();

  private Movie movie2 = Movie.builder()
      .id(1L)
      .country("US")
      .title("쇼생크 탈출")
      .director("프랭크 다라본트")
      .movieImage(
          "https://movie-phinf.pstatic.net/20160119_278/14531650465287bcuk_JPEG/movie_image.jpg?type=m203_290_2")
      .genre("DRAMA")
      .runningTime(142L)
      .summary("두려움은 너를 죄수로 가두고 희망은 너를 자유롭게 하리라")
      .build();

  private List<ActorInfo> actors = List.of(
      ActorInfo.builder().id(0L).name("조승우").role("고니").build(),
      ActorInfo.builder().id(1L).name("김혜수").role("정마담").build(),
      ActorInfo.builder().id(2L).name("백윤식").role("평강장").build(),
      ActorInfo.builder().id(3L).name("유해진").role("고광렬").build(),
      ActorInfo.builder().id(4L).name("김응수").role("곽철용").build()
  );
  private MovieDetailResponse movieDetailResponse1 = MovieDetailResponse.builder()
      .id(0L)
      .country("KR")
      .title("타짜")
      .director("최동훈")
      .movieImage(
          "https://movie-phinf.pstatic.net/20211122_91/1637564743461raGXe_JPEG/movie_image.jpg?type=m203_290_2")
      .genre("CRIME")
      .runningTime(139L)
      .summary("인생을 건 한판 승부 | 큰거 한판에 인생은 예술이 된다! | 목숨을 걸 수 없다면, 배팅하지 마라! | 꽃들의 전쟁")
      .rating(3.0)
      .actors(actors)
      .build();

  private MovieTitleResponse movieTitleResponse1 = MovieConverter.toMovieTitleResponse(movie1, 5.0);
  private MovieTitleResponse movieTitleResponse2 = MovieConverter.toMovieTitleResponse(movie2, 3.0);

  @Test
  void getMovieTitleList() {
    //given
    List<Movie> movies = List.of(movie1, movie2);

    given(movieRepository.findAll()).willReturn(movies);
    given(ratingService.getRatingScoreByMovieId(movie1.getId()))
        .willReturn(movieTitleResponse1.getRating());
    given(ratingService.getRatingScoreByMovieId(movie2.getId()))
        .willReturn(movieTitleResponse2.getRating());

    //when
    List<MovieTitleResponse> actual = movieService.getMovieTitleList();

    //then
    then(movieRepository).should().findAll();
    then(ratingService).should().getRatingScoreByMovieId(movie1.getId());
    then(ratingService).should().getRatingScoreByMovieId(movie2.getId());
    then(movieService).should().getMovieTitleList();

    List<MovieTitleResponse> expected = List.of(movieTitleResponse1, movieTitleResponse2);
    assertThat(actual.get(0), samePropertyValuesAs(expected.get(0)));
    assertThat(actual.get(1), samePropertyValuesAs(expected.get(1)));

  }

  @Test
  void getMovieById() {
    //given
    given(movieRepository.findMovieDetailById(movie1.getId())).willReturn(
        Optional.of(movieDetailResponse1));
    given(movieRepository.findActorsByMovieId(movie1.getId())).willReturn(actors);

    //when
    MovieDetailResponse actual = movieService.getMovieById(movie1.getId());

    //then
    then(movieRepository).should().findMovieDetailById(movie1.getId());
    then(movieService).should().getMovieById(movie1.getId());

    assertThat(actual.getId(), equalTo(movieDetailResponse1.getId()));
    assertThat(actual.getTitle(), equalTo(movieDetailResponse1.getTitle()));
    assertThat(actual.getMovieImage(), equalTo(movieDetailResponse1.getMovieImage()));
    assertThat(actual.getGenre(), equalTo(movieDetailResponse1.getGenre()));
    assertThat(actual.getCountry(), equalTo(movieDetailResponse1.getCountry()));
    assertThat(actual.getRunningTime(), equalTo(movieDetailResponse1.getRunningTime()));
    assertThat(actual.getSummary(), equalTo(movieDetailResponse1.getSummary()));
    assertThat(actual.getDirector(), equalTo(movieDetailResponse1.getDirector()));
    assertThat(actual.getRating(), equalTo(movieDetailResponse1.getRating()));
    assertThat(actual.getActors(), samePropertyValuesAs(movieDetailResponse1.getActors()));

  }

  @Test
  void getMovieByIdNotExist() {
    //given
    given(movieRepository.findMovieDetailById(3L))
        .willReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class, () -> movieService.getMovieById(3L));

    //then
    then(movieRepository).should().findMovieDetailById(3L);
    then(movieService).should().getMovieById(3L);


  }
}

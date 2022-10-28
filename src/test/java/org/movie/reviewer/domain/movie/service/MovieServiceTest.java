package org.movie.reviewer.domain.movie.service;

import static org.hamcrest.MatcherAssert.assertThat;
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
import org.movie.reviewer.domain.movie.dto.response.MovieResponse;
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

  private MovieResponse movieResponse1 = MovieConverter.toMovieResponse(movie1);
  private MovieResponse movieResponse2 = MovieConverter.toMovieResponse(movie2);

  private MovieTitleResponse movieTitleResponse1 =
      MovieConverter.toMovieTitleResponse(MovieConverter.toMovieTitleResponse(movie1), 5.0);
  private MovieTitleResponse movieTitleResponse2 =
      MovieConverter.toMovieTitleResponse(MovieConverter.toMovieTitleResponse(movie2), 3.0);

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
    given(movieRepository.findMovieById(movie1.getId())).willReturn(Optional.of(movie1));

    //when
    MovieResponse actual = movieService.getMovieById(movie1.getId());

    //then
    then(movieRepository).should().findMovieById(movie1.getId());
    then(movieService).should().getMovieById(movie1.getId());

    assertThat(actual, samePropertyValuesAs(movieResponse1));

  }

  @Test
  void getMovieByIdNotExist() {
    //given
    given(movieRepository.findMovieById(3L))
        .willReturn(Optional.empty());

    //when
    assertThrows(NotFoundException.class, () -> movieService.getMovieById(3L));

    //then
    then(movieRepository).should().findMovieById(3L);
    then(movieService).should().getMovieById(3L);


  }
}

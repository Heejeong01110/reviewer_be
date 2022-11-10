package org.movie.reviewer.domain.rating.service;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.RatingConverter;
import org.movie.reviewer.domain.rating.dto.response.RatingResponse;
import org.movie.reviewer.domain.rating.repository.RatingRepository;
import org.movie.reviewer.domain.user.domain.User;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

  @Spy
  @InjectMocks
  private RatingService ratingService;

  @Mock
  private RatingRepository ratingRepository;

  private Movie movie = Movie.builder()
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

  private User user = User.builder()
      .id(0L)
      .email("movieStar@gmail.com")
      .nickname("movieStar11")
      .password("test1234")
      .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
      .profileImage(null)
      .build();

  private Rating rating1 = Rating.builder()
      .id(0L)
      .contents("인생영화임..")
      .rating(5.0)
      .user(user)
      .movie(movie)
      .build();

  private Rating rating2 = Rating.builder()
      .id(1L)
      .contents("멋진 캐릭터, 흥미로운 이야기, 감각적인 스타일")
      .rating(4.5)
      .user(user)
      .movie(movie)
      .build();

  @Test
  void getRatingScoreByMovieId() {
    //given
    Double actual = rating1.getRating() + rating2.getRating() / 2;
    given(ratingRepository.getRatingAvgByMovieId(movie.getId())).willReturn(actual);

    //when
    Double expected = ratingService.getRatingScoreByMovieId(movie.getId());

    //then
    then(ratingService).should().getRatingScoreByMovieId(movie.getId());
    then(ratingRepository).should().getRatingAvgByMovieId(movie.getId());
    assertThat(actual, is(expected));
  }

  @Test
  void getRatingsByMovieId() {
    //given
    List<RatingResponse> expected = List.of(
        RatingConverter.toRatingResponse(rating1),
        RatingConverter.toRatingResponse(rating2));

    given(ratingRepository.getRatingsByMovieId(movie.getId())).willReturn(List.of(rating1, rating2));

    //when
    List<RatingResponse> actual = ratingService.getRatingsByMovieId(movie.getId());

    //then
    then(ratingService).should().getRatingsByMovieId(movie.getId());
    then(ratingRepository).should().getRatingsByMovieId(movie.getId());

    assertThat(actual.size(), is(2));
    assertThat(actual.size(), is(actual.size()));
    for(int i=0;i<actual.size();i++){
      assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
      assertThat(actual.get(i).getContents(), is(expected.get(i).getContents()));
      assertThat(actual.get(i).getRating(), is(expected.get(i).getRating()));
      assertThat(actual.get(i).getUser(), samePropertyValuesAs(expected.get(i).getUser()));
    }
  }
}

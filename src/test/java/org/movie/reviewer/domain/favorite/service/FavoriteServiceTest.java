package org.movie.reviewer.domain.favorite.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.favorite.domain.Favorite;
import org.movie.reviewer.domain.favorite.dto.FavoriteConverter;
import org.movie.reviewer.domain.favorite.dto.response.UserFavoriteResponse;
import org.movie.reviewer.domain.favorite.repository.FavoriteRepository;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.user.domain.User;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

  @Spy
  @InjectMocks
  private FavoriteService favoriteService;

  @Mock
  private FavoriteRepository favoriteRepository;


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

  private User user = User.builder()
      .id(0L)
      .email("movieStar@gmail.com")
      .nickname("movieStar11")
      .password("test1234")
      .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
      .profileImage(null)
      .build();

  private Favorite favorite1 = Favorite.builder()
      .id(0L)
      .isFavorite(true)
      .user(user)
      .movie(movie1)
      .build();

  private Favorite favorite2 = Favorite.builder()
      .id(1L)
      .isFavorite(false)
      .user(user)
      .movie(movie2)
      .build();

  @Test
  void getFavoritesByUserId() {
    //given
    List<UserFavoriteResponse> expected = List.of(
        FavoriteConverter.toUserFavoriteResponse(favorite1),
        FavoriteConverter.toUserFavoriteResponse(favorite2));

    given(favoriteRepository.findFavoritesByUserId(user.getId())).willReturn(List.of(favorite1, favorite2));

    //when
    List<UserFavoriteResponse> actual = favoriteService.getFavoritesByUserId(user.getId());

    //then
    then(favoriteService).should().getFavoritesByUserId(user.getId());
    then(favoriteRepository).should().findFavoritesByUserId(user.getId());

    assertThat(actual.size(), is(2));
    assertThat(actual.size(), is(actual.size()));
    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
      assertThat(actual.get(i).getMovie(), samePropertyValuesAs(expected.get(i).getMovie()));
    }

  }

}

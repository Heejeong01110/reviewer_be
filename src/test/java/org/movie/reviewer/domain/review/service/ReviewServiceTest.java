package org.movie.reviewer.domain.review.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.BDDMockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.response.MovieSimpleInfo;
import org.movie.reviewer.domain.movie.repository.MovieRepository;
import org.movie.reviewer.domain.movie.service.MovieService;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.repository.ReviewRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Spy
  @InjectMocks
  private ReviewService reviewService;

  @Mock
  private ReviewRepository reviewRepository;

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

  private Review review1 = Review.builder()
      .id(0L)
      .title("결국, 고전이 되었나보다.")
      .contents("개봉 당시에는 이게 고전이 되리라고 생각해본 적 없다. 그러나 뤽 베송의 재능이 쪼그라든 지금 다시 보자니, 울컥하는 감정이 치밀만큼 아름다운 데가 있다.")
      .user(user)
      .movie(movie)
      .build();

  private Review review2 = Review.builder()
      .id(0L)
      .title("킬러의 전설")
      .contents("고독한 킬러, 화분을 든 소녀, 광기의 경찰. 그리고 'Shape of My Heart'의 엔딩. 킬러에 대한 수많은 영화들이 있지만 [레옹]처럼 심금을 울리는 작품은 많지 않다.")
      .user(user)
      .movie(movie)
      .build();

  private ReviewTitleInfo info1 = ReviewTitleInfo.builder()
      .reviewId(review1.getId())
      .reviewTitle(review1.getTitle())
      .commentCount(3L)
      .userId(review1.getUser().getId())
      .nickname(review1.getUser().getNickname())
      .profileImage(review1.getUser().getProfileImage())
      .movieId(review1.getMovie().getId())
      .movieTitle(review1.getMovie().getTitle())
      .movieImage(review1.getMovie().getMovieImage())
      .build();

  private ReviewTitleInfo info2 = ReviewTitleInfo.builder()
      .reviewId(review2.getId())
      .reviewTitle(review2.getTitle())
      .commentCount(10L)
      .userId(review2.getUser().getId())
      .nickname(review2.getUser().getNickname())
      .profileImage(review2.getUser().getProfileImage())
      .movieId(review2.getMovie().getId())
      .movieTitle(review2.getMovie().getTitle())
      .movieImage(review2.getMovie().getMovieImage())
      .build();

  private ReviewTitleResponse response1 = ReviewConverter.toReviewTitleResponse(info1);

  private ReviewTitleResponse response2 = ReviewConverter.toReviewTitleResponse(info2);


  @Test
  void getReviewTitleList() {
    //given
    List<ReviewTitleResponse> expected = List.of(response1, response2);
    List<ReviewTitleInfo> infos = List.of(info1, info2);

    given(reviewRepository.findReviewTitleAll()).willReturn(infos);

    //when
    List<ReviewTitleResponse> actual = reviewService.getReviewTitleList();

    //then
    then(reviewRepository).should().findReviewTitleAll();
    then(reviewService).should().getReviewTitleList();

    assertThat(actual.size(), is(2));
    assertThat(actual.size(), is(expected.size()));

    assertThat(actual.get(0).getTitle(), is(equalTo(expected.get(0).getTitle())));
    assertThat(actual.get(0).getId(), is(expected.get(0).getId()));
    assertThat(actual.get(0).getCommentCount(), is(expected.get(0).getCommentCount()));
    assertThat(actual.get(0).getUser(), samePropertyValuesAs(expected.get(0).getUser()));
    assertThat(actual.get(0).getMovie(), samePropertyValuesAs(expected.get(0).getMovie()));

    assertThat(actual.get(1).getTitle(), is(equalTo(expected.get(1).getTitle())));
    assertThat(actual.get(1).getId(), is(expected.get(1).getId()));
    assertThat(actual.get(1).getCommentCount(), is(expected.get(1).getCommentCount()));
    assertThat(actual.get(1).getUser(), samePropertyValuesAs(expected.get(1).getUser()));
    assertThat(actual.get(1).getMovie(), samePropertyValuesAs(expected.get(1).getMovie()));

  }
}

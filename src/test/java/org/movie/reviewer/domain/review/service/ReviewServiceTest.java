package org.movie.reviewer.domain.review.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
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
import org.movie.reviewer.domain.movie.dto.response.MovieCardInfo;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewSimpleResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.repository.ReviewRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.response.UserSimpleInfo;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Spy
  @InjectMocks
  private ReviewService reviewService;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private RatingService ratingService;

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
      .role(UserRole.ROLE_MEMBER)
      .build();

  private Review review1 = Review.builder()
      .id(0L)
      .title("결국, 고전이 되었나보다.")
      .contents(
          "개봉 당시에는 이게 고전이 되리라고 생각해본 적 없다. 그러나 뤽 베송의 재능이 쪼그라든 지금 다시 보자니, 울컥하는 감정이 치밀만큼 아름다운 데가 있다.")
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
  private ReviewTitleResponse response1 = ReviewConverter.toReviewTitleResponse(info1);
  private Review review2 = Review.builder()
      .id(0L)
      .title("킬러의 전설")
      .contents(
          "고독한 킬러, 화분을 든 소녀, 광기의 경찰. 그리고 'Shape of My Heart'의 엔딩. 킬러에 대한 수많은 영화들이 있지만 [레옹]처럼 심금을 울리는 작품은 많지 않다.")
      .user(user)
      .movie(movie)
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

    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i).getTitle(), is(equalTo(expected.get(i).getTitle())));
      assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
      assertThat(actual.get(i).getCommentCount(), is(expected.get(i).getCommentCount()));
      assertThat(actual.get(i).getUser(), samePropertyValuesAs(expected.get(i).getUser()));
      assertThat(actual.get(i).getMovie(), samePropertyValuesAs(expected.get(i).getMovie()));
    }

  }

  @Test
  void getReviewById() {
    //given
    MovieCardInfo movieCardInfo = MovieConverter.toMovieCardInfo(review1.getMovie(), 4.5);
    UserSimpleInfo userSimpleInfo = UserConverter.toUserSimpleInfo(review1.getUser());

    given(reviewRepository.findReviewDetailById(review1.getId())).willReturn(
        Optional.of(review1));
    given(ratingService.getRatingScoreByMovieId(review1.getMovie().getId())).willReturn(
        movieCardInfo.getRating());

    //when
    ReviewDetailResponse actual = reviewService.getReviewById(review1.getId());

    //then
    then(reviewRepository).should().findReviewDetailById(review1.getId());
    then(ratingService).should().getRatingScoreByMovieId(review1.getMovie().getId());

    assertThat(actual.getId(), is(review1.getId()));
    assertThat(actual.getTitle(), equalTo(review1.getTitle()));
    assertThat(actual.getContents(), equalTo(review1.getContents()));
    assertThat(actual.getMovie(), samePropertyValuesAs(movieCardInfo));
    assertThat(actual.getUser(), samePropertyValuesAs(userSimpleInfo));
  }

  @Test
  void getSimpleReviewsByMovieId() {
    //given
    ReviewSimpleResponse response1 = ReviewSimpleResponse.builder()
        .id(review1.getId())
        .title(review1.getTitle())
        .contents(review1.getContents())
        .updatedAt(LocalDateTime.now())
        .user(UserSimpleInfo.builder()
            .id(review1.getUser().getId())
            .nickname(review1.getUser().getNickname())
            .profileImage(review1.getUser().getProfileImage())
            .build())
        .build();

    ReviewSimpleResponse response2 = ReviewSimpleResponse.builder()
        .id(review2.getId())
        .title(review2.getTitle())
        .contents(review2.getContents())
        .updatedAt(LocalDateTime.now())
        .user(UserSimpleInfo.builder()
            .id(review2.getUser().getId())
            .nickname(review2.getUser().getNickname())
            .profileImage(review2.getUser().getProfileImage())
            .build())
        .build();

    List<ReviewSimpleResponse> expected = List.of(response1, response2);

    given(reviewRepository.findReviewsByMovieId(movie.getId()))
        .willReturn(List.of(review1, review2));

    //when
    List<ReviewSimpleResponse> actual = reviewService.getSimpleReviewsByMovieId(movie.getId());

    //then
    then(reviewService).should().getSimpleReviewsByMovieId(movie.getId());
    then(reviewRepository).should().findReviewsByMovieId(movie.getId());

    assertThat(actual.size(), is(2));
    assertThat(actual.size(), is(expected.size()));

    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
      assertThat(actual.get(i).getTitle(), equalTo(expected.get(i).getTitle()));
      assertThat(actual.get(i).getContents(), equalTo(expected.get(i).getContents()));
      assertThat(actual.get(i).getUser(), samePropertyValuesAs(expected.get(i).getUser()));
    }

  }

  @Test
  void getReviewsByUserId() {
    //given
    List<UserReviewResponse> expected = List.of(
        ReviewConverter.toUserReviewResponse(review1),
        ReviewConverter.toUserReviewResponse(review2));

    given(reviewRepository.findReviewsByUserId(user.getId())).willReturn(List.of(review1, review2));

    //when
    List<UserReviewResponse> actual = reviewService.getReviewsByUserId(user.getId());

    //then
    then(reviewService).should().getReviewsByUserId(user.getId());
    then(reviewRepository).should().findReviewsByUserId(user.getId());

    assertThat(actual.size(), is(2));
    assertThat(actual.size(), is(actual.size()));
    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
      assertThat(actual.get(i).getTitle(), is(expected.get(i).getTitle()));
      assertThat(actual.get(i).getLikeCount(), is(expected.get(i).getLikeCount()));
      assertThat(actual.get(i).getUpdatedAt(), is(expected.get(i).getUpdatedAt()));
      assertThat(actual.get(i).getMovie(), samePropertyValuesAs(expected.get(i).getMovie()));
    }

  }

}

package org.movie.reviewer.domain.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
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
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.RatingConverter;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.service.ReviewService;
import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Spy
  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ReviewService reviewService;

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

  private Review review = Review.builder()
      .id(0L)
      .title("결국, 고전이 되었나보다.")
      .contents(
          "개봉 당시에는 이게 고전이 되리라고 생각해본 적 없다. 그러나 뤽 베송의 재능이 쪼그라든 지금 다시 보자니, 울컥하는 감정이 치밀만큼 아름다운 데가 있다.")
      .user(user)
      .movie(movie)
      .build();
  private ReviewComment reviewComment = ReviewComment.builder()
      .id(0L)
      .contents("공감합니다1")
      .likeCount(3L)
      .user(user)
      .review(review).build();
  private Rating rating = Rating.builder()
      .id(0L)
      .contents("인생영화임..")
      .rating(5.0)
      .user(user)
      .movie(movie)
      .build();

  @Test
  void getUserById() {
    //given
    List<UserReviewResponse> reviews = List.of(ReviewConverter.toUserReviewResponse(review));
    List<UserRatingResponse> ratings = List.of(RatingConverter.toUserRatingResponse(rating));
    UserDetailResponse expected =
        UserConverter.toUserDetailResponse(user, reviews, ratings);

    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(reviewService.getReviewsByUserId(user.getId())).willReturn(reviews);
    given(ratingService.getRatingsByUserId(user.getId())).willReturn(ratings);

    //when
    UserDetailResponse actual = userService.getUserById(user.getId());

    //then
    then(userService).should().getUserById(user.getId());
    then(userRepository).should().findById(user.getId());
    then(reviewService).should().getReviewsByUserId(user.getId());
    then(ratingService).should().getRatingsByUserId(user.getId());

    assertThat(actual.getId(), is(expected.getId()));
    assertThat(actual.getNickname(), is(expected.getNickname()));
    assertThat(actual.getProfileImage(), is(expected.getProfileImage()));
    assertThat(actual.getIntroduction(), is(expected.getIntroduction()));

    assertThat(actual.getReviews().size(), is(1));
    assertThat(actual.getReviews().size(), is(actual.getReviews().size()));
    for (int i = 0; i < actual.getReviews().size(); i++) {
      assertThat(actual.getReviews().get(i), samePropertyValuesAs(expected.getReviews().get(i)));
    }

    assertThat(actual.getRatings().size(), is(1));
    assertThat(actual.getRatings().size(), is(actual.getRatings().size()));
    for (int i = 0; i < actual.getRatings().size(); i++) {
      assertThat(actual.getRatings().get(i), samePropertyValuesAs(expected.getRatings().get(i)));
    }
  }

  @Test
  void checkEmailDuplicate() {
    //given
    given(userRepository.existsByEmail(user.getEmail())).willReturn(true);

    //when
    boolean actual = userService.checkEmailDuplicate(user.getEmail());

    //then
    then(userService).should().checkEmailDuplicate(user.getEmail());
    then(userRepository).should().existsByEmail(user.getEmail());

    assertThat(actual, is(false));
  }

  @Test
  void checkNicknameDuplicate() {
    //given
    given(userRepository.existsByNickname(user.getNickname())).willReturn(true);

    //when
    boolean actual = userService.checkNicknameDuplicate(user.getNickname());

    //then
    then(userService).should().checkNicknameDuplicate(user.getNickname());
    then(userRepository).should().existsByNickname(user.getNickname());

    assertThat(actual, is(false));
  }
}

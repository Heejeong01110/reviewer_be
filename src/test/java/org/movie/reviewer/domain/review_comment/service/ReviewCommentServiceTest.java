package org.movie.reviewer.domain.review_comment.service;


import static org.hamcrest.CoreMatchers.equalTo;
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
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.movie.reviewer.domain.review_comment.dto.ReviewCommentConverter;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.dto.response.UserCommentResponse;
import org.movie.reviewer.domain.review_comment.repository.ReviewCommentRepository;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;

@ExtendWith(MockitoExtension.class)
class ReviewCommentServiceTest {

  @Spy
  @InjectMocks
  private ReviewCommentService reviewCommentService;

  @Mock
  private ReviewCommentRepository reviewCommentRepository;

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
      .authority(UserRole.ROLE_MEMBER)
      .build();

  private Review review = Review.builder()
      .id(0L)
      .title("결국, 고전이 되었나보다.")
      .contents(
          "개봉 당시에는 이게 고전이 되리라고 생각해본 적 없다. 그러나 뤽 베송의 재능이 쪼그라든 지금 다시 보자니, 울컥하는 감정이 치밀만큼 아름다운 데가 있다.")
      .user(user)
      .movie(movie)
      .build();

  private ReviewComment reviewComment1 = ReviewComment.builder()
      .id(0L)
      .contents("공감합니다1")
      .likeCount(3L)
      .user(user)
      .review(review).build();

  private ReviewComment reviewComment2 = ReviewComment.builder()
      .id(1L)
      .contents("공감합니다2")
      .likeCount(2L)
      .user(user)
      .review(review).build();

  private ReviewComment reviewComment3 = ReviewComment.builder()
      .id(2L)
      .contents("공감합니다3")
      .likeCount(5L)
      .user(user)
      .review(review).build();

  @Test
  void getReviewComments() {
    //given
    List<ReviewCommentResponse> expected = List.of(
        ReviewCommentConverter.toReviewCommentResponse(reviewComment1),
        ReviewCommentConverter.toReviewCommentResponse(reviewComment2),
        ReviewCommentConverter.toReviewCommentResponse(reviewComment3));

    given(reviewCommentRepository.findCommentsByReviewId(review.getId()))
        .willReturn(List.of(reviewComment1, reviewComment2, reviewComment3));

    //when
    List<ReviewCommentResponse> actual =
        reviewCommentService.getReviewComments(review.getId());

    //then
    then(reviewCommentService).should().getReviewComments(review.getId());
    then(reviewCommentRepository).should().findCommentsByReviewId(review.getId());

    assertThat(actual.size(), is(3));
    assertThat(actual.size(), is(expected.size()));

    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i).getId(), is(expected.get(i).getId()));
      assertThat(actual.get(i).getContents(), is(equalTo(expected.get(i).getContents())));
      assertThat(actual.get(i).getLikeCount(), is(expected.get(i).getLikeCount()));
      assertThat(actual.get(i).getUpdatedAt(), is(expected.get(i).getUpdatedAt()));
      assertThat(actual.get(i).getUser(), samePropertyValuesAs(expected.get(i).getUser()));
    }

  }

  @Test
  void getCommentsByUserId() {
    //given
    List<UserCommentResponse> expected = List.of(
        ReviewCommentConverter.toUserCommentResponse(reviewComment1),
        ReviewCommentConverter.toUserCommentResponse(reviewComment2),
        ReviewCommentConverter.toUserCommentResponse(reviewComment3));

    given(reviewCommentRepository.findCommentsByUserId(user.getId()))
        .willReturn(List.of(reviewComment1, reviewComment2, reviewComment3));

    //when
    List<UserCommentResponse> actual = reviewCommentService.getCommentsByUserId(user.getId());

    //then
    then(reviewCommentService).should().getCommentsByUserId(user.getId());
    then(reviewCommentRepository).should().findCommentsByUserId(user.getId());

    assertThat(actual.size(), is(3));
    assertThat(actual.size(), is(actual.size()));
    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i), samePropertyValuesAs(expected.get(i)));
    }

  }
}

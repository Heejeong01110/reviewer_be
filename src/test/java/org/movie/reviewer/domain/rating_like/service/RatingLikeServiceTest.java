package org.movie.reviewer.domain.rating_like.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.repository.RatingRepository;
import org.movie.reviewer.domain.rating_like.domain.LikeType;
import org.movie.reviewer.domain.rating_like.domain.RatingLike;
import org.movie.reviewer.domain.rating_like.repository.RatingLikeRepository;
import org.movie.reviewer.domain.review_like.service.ReviewLikeService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.domain.user.repository.UserRepository;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RatingLikeServiceTest {

  @Spy
  @InjectMocks
  private RatingLikeService ratingLikeService;

  @Mock
  private RatingLikeRepository ratingLikeRepository;

  @Mock
  private RatingRepository ratingRepository;

  @Mock
  private UserRepository userRepository;

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
      .profileImage(
          "https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
      .authority(UserRole.ROLE_MEMBER)
      .build();

  private User user2 = User.builder()
      .id(1L)
      .email("movieStar2@gmail.com")
      .nickname("movieStar22")
      .password("test1234")
      .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
      .profileImage(
          "https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
      .authority(UserRole.ROLE_MEMBER)
      .build();

  private Rating rating = Rating.builder()
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
      .user(user2)
      .movie(movie)
      .build();

  private RatingLike ratingLike = RatingLike.builder()
      .id(0L)
      .likeType(0L)
      .user(user)
      .rating(rating)
      .build();


  @Test
  void getRatingLike_UndefineToLike() {
    //given
    Long likeType = LikeType.LIKE.getNum();
    RatingLike expected = RatingLike.builder()
        .id(1L)
        .likeType(LikeType.UNDEFINE.getNum())
        .user(user)
        .rating(rating2)
        .build();
    ReflectionTestUtils.setField(expected, "likeType", likeType);

    given(ratingRepository.findById(rating.getId())).willReturn(Optional.of(rating));
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    given(ratingLikeRepository.existsByUserIdAndRatingId(user.getId(), rating.getId())).willReturn(false);
    given(ratingLikeRepository.save(any())).willReturn(expected);

    //when
    RatingLike actual = ratingLikeService.updateRatingLike(
        user.getEmail(), rating.getId(),LikeType.LIKE.getNum());

    //then
    then(ratingLikeService).should().updateRatingLike(user.getEmail(),rating.getId(), likeType);
    then(ratingRepository).should().findById(rating.getId());
    then(userRepository).should().findByEmail(user.getEmail());
    then(ratingLikeRepository).should().existsByUserIdAndRatingId(user.getId(), rating.getId());
    then(ratingLikeRepository).should().save(any());

    assertThat(actual, samePropertyValuesAs(expected));
  }


  @Test
  void getRatingLike_UndefineToDislike() {
    //given
    Long likeType = LikeType.DISLIKE.getNum();
    RatingLike expected = RatingLike.builder()
        .id(1L)
        .likeType(LikeType.UNDEFINE.getNum())
        .user(user)
        .rating(rating2)
        .build();
    ReflectionTestUtils.setField(expected, "likeType", likeType);

    given(ratingRepository.findById(rating.getId())).willReturn(Optional.of(rating));
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    given(ratingLikeRepository.existsByUserIdAndRatingId(user.getId(), rating.getId())).willReturn(false);
    given(ratingLikeRepository.save(any())).willReturn(expected);

    //when
    RatingLike actual = ratingLikeService.updateRatingLike(
        user.getEmail(), rating.getId(),LikeType.DISLIKE.getNum());

    //then
    then(ratingLikeService).should().updateRatingLike(user.getEmail(),rating.getId(), likeType);
    then(ratingRepository).should().findById(rating.getId());
    then(userRepository).should().findByEmail(user.getEmail());
    then(ratingLikeRepository).should().existsByUserIdAndRatingId(user.getId(), rating.getId());
    then(ratingLikeRepository).should().save(any());

    assertThat(actual, samePropertyValuesAs(expected));
  }

  @Test
  void getRatingLike_DislikeToLike() {
    //given
    Long likeType = LikeType.LIKE.getNum();
    RatingLike expected = RatingLike.builder()
        .id(1L)
        .likeType(LikeType.DISLIKE.getNum())
        .user(user)
        .rating(rating2)
        .build();
    ReflectionTestUtils.setField(expected, "likeType", likeType);

    given(ratingRepository.findById(rating.getId())).willReturn(Optional.of(rating));
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    given(ratingLikeRepository.existsByUserIdAndRatingId(user.getId(), rating.getId())).willReturn(false);
    given(ratingLikeRepository.save(any())).willReturn(expected);

    //when
    RatingLike actual = ratingLikeService.updateRatingLike(
        user.getEmail(), rating.getId(),LikeType.LIKE.getNum());

    //then
    then(ratingLikeService).should().updateRatingLike(user.getEmail(),rating.getId(), likeType);
    then(ratingRepository).should().findById(rating.getId());
    then(userRepository).should().findByEmail(user.getEmail());
    then(ratingLikeRepository).should().existsByUserIdAndRatingId(user.getId(), rating.getId());
    then(ratingLikeRepository).should().save(any());

    assertThat(actual, samePropertyValuesAs(expected));
  }

  @Test
  void getRatingLike_DislikeToUndefine() {
    //given
    Long likeType = LikeType.DISLIKE.getNum();
    RatingLike expected = RatingLike.builder()
        .id(1L)
        .likeType(LikeType.UNDEFINE.getNum())
        .user(user)
        .rating(rating2)
        .build();
    ReflectionTestUtils.setField(expected, "likeType", likeType);

    given(ratingRepository.findById(rating.getId())).willReturn(Optional.of(rating));
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    given(ratingLikeRepository.existsByUserIdAndRatingId(user.getId(), rating.getId())).willReturn(false);
    given(ratingLikeRepository.save(any())).willReturn(expected);

    //when
    RatingLike actual = ratingLikeService.updateRatingLike(
        user.getEmail(), rating.getId(),LikeType.DISLIKE.getNum());

    //then
    then(ratingLikeService).should().updateRatingLike(user.getEmail(),rating.getId(), likeType);
    then(ratingRepository).should().findById(rating.getId());
    then(userRepository).should().findByEmail(user.getEmail());
    then(ratingLikeRepository).should().existsByUserIdAndRatingId(user.getId(), rating.getId());
    then(ratingLikeRepository).should().save(any());

    assertThat(actual, samePropertyValuesAs(expected));
  }
}

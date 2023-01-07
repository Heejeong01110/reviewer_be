package org.movie.reviewer.domain.review.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.ReviewDetailResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewSimpleResponse;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleInfo;
import org.movie.reviewer.domain.review.dto.response.ReviewTitleResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.review.service.ReviewService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.global.security.config.WebSecurityConfig;
import org.movie.reviewer.global.security.config.WithMockCustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = ReviewApi.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class reviewApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ReviewService reviewService;

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

  private Review review1 = Review.builder()
      .id(0L)
      .title("결국, 고전이 되었나보다.")
      .contents(
          "개봉 당시에는 이게 고전이 되리라고 생각해본 적 없다. 그러나 뤽 베송의 재능이 쪼그라든 지금 다시 보자니, 울컥하는 감정이 치밀만큼 아름다운 데가 있다.")
      .likeCount(3L)
      .user(user)
      .movie(movie)
      .build();
  private Review review2 = Review.builder()
      .id(0L)
      .title("킬러의 전설")
      .contents(
          "고독한 킬러, 화분을 든 소녀, 광기의 경찰. 그리고 'Shape of My Heart'의 엔딩. 킬러에 대한 수많은 영화들이 있지만 [레옹]처럼 심금을 울리는 작품은 많지 않다.")
      .user(user)
      .likeCount(1L)
      .movie(movie)
      .build();

  private DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private LocalDateTime getDateTime(String date) {
    return LocalDateTime.parse(date, simpleDateFormat);
  }

  @Test
  @WithMockCustomUser
  void whenGetReviews_thenReturnReviews() throws Exception {
    //given
    ReviewTitleInfo info1 = ReviewTitleInfo.builder()
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
    ReviewTitleInfo info2 = ReviewTitleInfo.builder()
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
    List<ReviewTitleResponse> reviews = List.of(
        ReviewConverter.toReviewTitleResponse(info1),
        ReviewConverter.toReviewTitleResponse(info2));

    given(reviewService.getReviewTitleList()).willReturn(reviews);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("review/get-reviews",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("리뷰 고유번호"),
                fieldWithPath("[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("[].commentCount").type(JsonFieldType.NUMBER).description("댓글 갯수"),
                fieldWithPath("[].user").type(JsonFieldType.OBJECT).description("유저"),
                fieldWithPath("[].user.id").type(JsonFieldType.NUMBER).description("유저 고유번호"),
                fieldWithPath("[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                fieldWithPath("[].user.profileImage").type(JsonFieldType.STRING)
                    .description("유저 프로필 url"),
                fieldWithPath("[].movie").type(JsonFieldType.OBJECT).description("영화"),
                fieldWithPath("[].movie.id").type(JsonFieldType.NUMBER).description("영화 고유번호"),
                fieldWithPath("[].movie.title").type(JsonFieldType.STRING).description("영화명"),
                fieldWithPath("[].movie.movieImage").type(JsonFieldType.STRING)
                    .description("영화 포스터 url")
            )
        ));
  }


  @Test
  @WithMockCustomUser
  void givenReviewId_whenGetReview_thenReturnReview() throws Exception {
    //given
    ReflectionTestUtils.setField(review1, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(review1, "updatedAt", getDateTime("2023-09-05 12:36:04"));

    ReviewDetailResponse review = ReviewConverter.toReviewDetailResponse(review1, 4.5);
    given(reviewService.getReviewById(review1.getId())).willReturn(review);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/reviews/{reviewId}", review1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("review/get-review-by-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("reviewId").description("리뷰 고유번호")),
            responseFields(
                fieldWithPath(".id").type(JsonFieldType.NUMBER).description("리뷰 고유번호"),
                fieldWithPath(".title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath(".contents").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath(".updatedAt").type(JsonFieldType.STRING).description("업데이트 시간"),
                fieldWithPath(".user").type(JsonFieldType.OBJECT).description("유저"),
                fieldWithPath(".user.id").type(JsonFieldType.NUMBER).description("유저 고유번호"),
                fieldWithPath(".user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                fieldWithPath(".user.profileImage").type(JsonFieldType.STRING)
                    .description("유저 프로필 url"),
                fieldWithPath(".movie").type(JsonFieldType.OBJECT).description("영화"),
                fieldWithPath(".movie.id").type(JsonFieldType.NUMBER).description("영화 고유번호"),
                fieldWithPath(".movie.title").type(JsonFieldType.STRING).description("영화명"),
                fieldWithPath(".movie.movieImage").type(JsonFieldType.STRING)
                    .description("영화 포스터 url"),
                fieldWithPath(".movie.genre").type(JsonFieldType.STRING).description("장르"),
                fieldWithPath(".movie.country").type(JsonFieldType.STRING).description("국가"),
                fieldWithPath(".movie.runningTime").type(JsonFieldType.NUMBER).description("러닝 타임"),
                fieldWithPath(".movie.rating").type(JsonFieldType.NUMBER).description("평점")
            )
        ));
  }

  @Test
  @WithMockCustomUser
  void givenMovieId_whenGetMovieReviews_thenReturnReviews() throws Exception {
    //given
    ReflectionTestUtils.setField(review1, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(review1, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(review2, "createdAt", getDateTime("2022-10-05 12:34:04"));
    ReflectionTestUtils.setField(review2, "updatedAt", getDateTime("2023-10-05 12:36:04"));

    List<ReviewSimpleResponse> reviews = List.of(
        ReviewConverter.toReviewSimpleResponse(review1),
        ReviewConverter.toReviewSimpleResponse(review2));
    given(reviewService.getSimpleReviewsByMovieId(movie.getId())).willReturn(reviews);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/movies/{movieId}/reviews", movie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("review/get-reviews-by-movieid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("movieId").description("영화 고유번호")),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("리뷰 고유번호"),
                fieldWithPath("[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("업데이트 시간"),
                fieldWithPath("[].user").type(JsonFieldType.OBJECT).description("유저"),
                fieldWithPath("[].user.id").type(JsonFieldType.NUMBER).description("유저 고유번호"),
                fieldWithPath("[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                fieldWithPath("[].user.profileImage").type(JsonFieldType.STRING)
                    .description("유저 프로필 url")
            )
        ));
  }

  @Test
  @WithMockCustomUser
  void givenUserId_whenGetReviewsByUserId_thenReturnReviews() throws Exception {
    //given
    ReflectionTestUtils.setField(review1, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(review1, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(review2, "createdAt", getDateTime("2022-10-05 12:34:04"));
    ReflectionTestUtils.setField(review2, "updatedAt", getDateTime("2023-10-05 12:36:04"));

    List<UserReviewResponse> reviews = List.of(
        ReviewConverter.toUserReviewResponse(review1),
        ReviewConverter.toUserReviewResponse(review2));
    given(reviewService.getReviewsByUserId(user.getId())).willReturn(reviews);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/accounts/{userId}/reviews", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("review/get-reviews-by-userid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("userId").description("유저 고유번호")),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("리뷰 고유번호"),
                fieldWithPath("[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("업데이트 시간"),
                fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 갯수"),
                fieldWithPath("[].movie").type(JsonFieldType.OBJECT).description("영화"),
                fieldWithPath("[].movie.id").type(JsonFieldType.NUMBER).description("영화 고유번호"),
                fieldWithPath("[].movie.title").type(JsonFieldType.STRING).description("영화명"),
                fieldWithPath("[].movie.movieImage").type(JsonFieldType.STRING)
                    .description("영화 포스터 url")
            )
        ));
  }

}

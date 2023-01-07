package org.movie.reviewer.domain.review_comment.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
import org.movie.reviewer.domain.review_comment.domain.ReviewComment;
import org.movie.reviewer.domain.review_comment.dto.ReviewCommentConverter;
import org.movie.reviewer.domain.review_comment.dto.response.ReviewCommentResponse;
import org.movie.reviewer.domain.review_comment.dto.response.UserCommentResponse;
import org.movie.reviewer.domain.review_comment.service.ReviewCommentService;
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
@WebMvcTest(controllers = ReviewCommentApi.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ReviewCommentApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ReviewCommentService reviewCommentService;

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

  private User user2 = User.builder()
      .id(1L)
      .email("flower@gmail.com")
      .nickname("goni00")
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
      .user(user2)
      .movie(movie)
      .build();

  private ReviewComment reviewComment1 = ReviewComment.builder()
      .id(0L)
      .contents("공감합니다")
      .likeCount(3L)
      .user(user2)
      .review(review1).build();

  private ReviewComment reviewComment2 = ReviewComment.builder()
      .id(3L)
      .contents("공감합니다")
      .likeCount(1L)
      .user(user2)
      .review(review1).build();

  private ReviewComment reviewComment3 = ReviewComment.builder()
      .id(6L)
      .contents("공감합니다")
      .likeCount(0L)
      .user(user2)
      .review(review1).build();

  private DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private LocalDateTime getDateTime(String date) {
    return LocalDateTime.parse(date, simpleDateFormat);
  }

  @Test
  @WithMockCustomUser
  void givenReviewId_whenGetReviewComments_thenReturnComments() throws Exception {
    //given
    ReflectionTestUtils.setField(reviewComment1, "createdAt", getDateTime("2022-09-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment1, "updatedAt", getDateTime("2023-10-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment2, "createdAt", getDateTime("2022-02-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment2, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment3, "createdAt", getDateTime("2022-09-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment3, "updatedAt", getDateTime("2023-10-05 12:36:04"));

    List<ReviewCommentResponse> comments = List.of(
        ReviewCommentConverter.toReviewCommentResponse(reviewComment1),
        ReviewCommentConverter.toReviewCommentResponse(reviewComment2),
        ReviewCommentConverter.toReviewCommentResponse(reviewComment3));
    given(reviewCommentService.getReviewComments(review1.getId())).willReturn(comments);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/reviews/{reviewId}/comments", review1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("reviewComment/get-reviewComment-by-reviewid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("댓글 고유번호"),
                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("댓글 내용"),
                fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 갯수"),
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
  void givenUserId_whenGetReviewCommentsByUserId_thenReturnComments() throws Exception {
    //given
    ReflectionTestUtils.setField(reviewComment1, "createdAt", getDateTime("2022-09-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment1, "updatedAt", getDateTime("2023-10-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment2, "createdAt", getDateTime("2022-02-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment2, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment3, "createdAt", getDateTime("2022-09-05 12:36:04"));
    ReflectionTestUtils.setField(reviewComment3, "updatedAt", getDateTime("2023-10-05 12:36:04"));

    List<UserCommentResponse> comments = List.of(
        ReviewCommentConverter.toUserCommentResponse(reviewComment1),
        ReviewCommentConverter.toUserCommentResponse(reviewComment2),
        ReviewCommentConverter.toUserCommentResponse(reviewComment3));
    given(reviewCommentService.getCommentsByUserId(user2.getId())).willReturn(comments);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/accounts/{userId}/comments", user2.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("reviewComment/get-reviewComment-by-userid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("댓글 고유번호"),
                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("댓글 내용"),
                fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 갯수"),
                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("업데이트 시간"),
                fieldWithPath("[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 고유번호")
            )
        ));
  }
}

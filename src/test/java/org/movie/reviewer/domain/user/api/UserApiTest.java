package org.movie.reviewer.domain.user.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.RatingConverter;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.review.domain.Review;
import org.movie.reviewer.domain.review.dto.ReviewConverter;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.domain.user.dto.UserConverter;
import org.movie.reviewer.domain.user.dto.request.SignUpRequest;
import org.movie.reviewer.domain.user.dto.response.UserDetailResponse;
import org.movie.reviewer.domain.user.service.UserService;
import org.movie.reviewer.global.security.annotation.WithMockCustomAnonymousUser;
import org.movie.reviewer.global.security.annotation.WithMockCustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

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
      .password("{bcrypt}$2a$10$HvKBACAuzrvJGvpKcb8S3O7RX8uqg72U/dD5TD/3L.ps3c9Ydng6i")
      .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
      .profileImage(
          "https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
      .authority(UserRole.ROLE_MEMBER)
      .build();

  private Review review = Review.builder()
      .id(0L)
      .title("결국, 고전이 되었나보다.")
      .contents(
          "개봉 당시에는 이게 고전이 되리라고 생각해본 적 없다. 그러나 뤽 베송의 재능이 쪼그라든 지금 다시 보자니, 울컥하는 감정이 치밀만큼 아름다운 데가 있다.")
      .likeCount(3L)
      .user(user)
      .movie(movie)
      .build();

  private Rating rating = Rating.builder()
      .id(0L)
      .likeCount(7L)
      .contents("인생영화임..")
      .rating(5.0)
      .user(user)
      .movie(movie)
      .build();

  private DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private LocalDateTime getDateTime(String date) {
    return LocalDateTime.parse(date, simpleDateFormat);
  }

  @Test
  @WithMockCustomAnonymousUser
  void givenUserId_whenGetUserById_thenReturnUser() throws Exception {
    //given
    ReflectionTestUtils.setField(review, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(review, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(rating, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(rating, "updatedAt", getDateTime("2023-09-05 12:36:04"));

    List<UserReviewResponse> reviews = List.of(ReviewConverter.toUserReviewResponse(review));
    List<UserRatingResponse> ratings = List.of(RatingConverter.toUserRatingResponse(rating));
    UserDetailResponse userResponse = UserConverter.toUserDetailResponse(user, reviews, ratings);

    given(userService.getUserById(user.getId())).willReturn(userResponse);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/accounts/{userId}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("accounts/get-user-by-userid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("userId").description("유저 고유번호")),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 고유번호"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개"),
                fieldWithPath("profileImage").type(JsonFieldType.STRING)
                    .description("유저 프로필 사진 url"),
                fieldWithPath("reviews").type(JsonFieldType.ARRAY).description("작성 리뷰"),
                fieldWithPath("reviews[].id").type(JsonFieldType.NUMBER).description("리뷰 고유번호"),
                fieldWithPath("reviews[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("reviews[].updatedAt").type(JsonFieldType.STRING)
                    .description("업데이트 시간"),
                fieldWithPath("reviews[].likeCount").type(JsonFieldType.NUMBER)
                    .description("좋아요 갯수"),
                fieldWithPath("reviews[].movie").type(JsonFieldType.OBJECT).description("영화"),
                fieldWithPath("reviews[].movie.id").type(JsonFieldType.NUMBER)
                    .description("영화 고유번호"),
                fieldWithPath("reviews[].movie.title").type(JsonFieldType.STRING)
                    .description("영화명"),
                fieldWithPath("reviews[].movie.movieImage").type(JsonFieldType.STRING)
                    .description("영화 포스터 사진 url"),
                fieldWithPath("ratings").type(JsonFieldType.ARRAY).description("작성 평점"),
                fieldWithPath("ratings[].id").type(JsonFieldType.NUMBER).description("평점 고유번호"),
                fieldWithPath("ratings[].contents").type(JsonFieldType.STRING).description("평점 내용"),
                fieldWithPath("ratings[].updatedAt").type(JsonFieldType.STRING)
                    .description("업데이트 시간"),
                fieldWithPath("ratings[].likeCount").type(JsonFieldType.NUMBER)
                    .description("좋아요 갯수"),
                fieldWithPath("ratings[].movie").type(JsonFieldType.OBJECT).description("영화"),
                fieldWithPath("ratings[].movie.id").type(JsonFieldType.NUMBER)
                    .description("영화 고유번호"),
                fieldWithPath("ratings[].movie.title").type(JsonFieldType.STRING)
                    .description("영화명"),
                fieldWithPath("ratings[].movie.movieImage").type(JsonFieldType.STRING)
                    .description("영화 포스터 사진 url")
            )
        ));

  }

  @Test
  @WithMockCustomAnonymousUser
  void givenUserEmail_whenCheckEmailDuplicate_thenReturnBoolean() throws Exception {
    //given
    given(userService.isDuplicatedEmail(user.getEmail())).willReturn(true);
    Map<String, Object> request = new HashMap<>();
    request.put("email", user.getEmail());

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/validity_checks/email", user.getEmail())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("validity_checks/email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestBody(request)
        ));
  }

  @Test
  @WithMockCustomAnonymousUser
  void givenUserNickname_whenCheckNicknameDuplicate_thenReturnBoolean() throws Exception {
    //given
    given(userService.isDuplicatedNickname(user.getNickname())).willReturn(true);
    Map<String, Object> request = new HashMap<>();
    request.put("nickname", user.getNickname());

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/validity_checks/nickname")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("validity_checks/nickname",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestBody(request)
        ));
  }

  @Test
  @WithMockCustomAnonymousUser
  void givenUser_whenSignup_thenReturnStatus() throws Exception {
    //given
    SignUpRequest request = SignUpRequest.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .password("test123")
        .build();

    given(userService.save(request)).willReturn(user);

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(document("accounts/signup",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }

  @Test
  @WithMockCustomUser
  void givenPassword_whenCheckPasswordValid_thenReturnBoolean() throws Exception {
    //given
    given(userService.checkPasswordValid(user, "test1234")).willReturn(true);
    Map<String, String> request = new HashMap<>();
    request.put("password", "test1234");

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/validity_checks/password")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("validity_checks/password",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }

  @Test
  @WithMockCustomUser
  void givenEmail_whenUpdateUserEmail_thenReturnUpdatedUser() throws Exception {
    //given
    String updatedEmail = "testEmail@gmail.com";
    User updatedUser = User.builder()
        .id(0L)
        .email(updatedEmail)
        .nickname("movieStar11")
        .password("{bcrypt}$2a$10$HvKBACAuzrvJGvpKcb8S3O7RX8uqg72U/dD5TD/3L.ps3c9Ydng6i")
        .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
        .profileImage("https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
        .authority(UserRole.ROLE_MEMBER)
        .build();
    given(userService.updateUserEmail(user, updatedEmail)).willReturn(updatedUser);
    Map<String, String> request = new HashMap<>();
    request.put("email", updatedEmail);

    //when
    ResultActions result = mockMvc.perform(
        put("/api/v1/accounts/email")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("accounts/email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }
}

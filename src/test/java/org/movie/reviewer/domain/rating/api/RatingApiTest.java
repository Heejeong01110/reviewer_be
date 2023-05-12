package org.movie.reviewer.domain.rating.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.RatingConverter;
import org.movie.reviewer.domain.rating.dto.request.RatingCreateRequest;
import org.movie.reviewer.domain.rating.dto.response.RatingResponse;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
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
class RatingApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
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
      .profileImage("https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
      .authority(UserRole.ROLE_MEMBER)
      .build();

  private Rating rating1 = Rating.builder()
      .id(0L)
      .likeCount(7L)
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
      .likeCount(2L)
      .build();

  private DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Test
  @WithMockCustomAnonymousUser
  void givenMovieId_whenGetRating_thenReturnMovieRating() throws Exception {
    //given
    ReflectionTestUtils.setField(rating1, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(rating1, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(rating2, "createdAt", getDateTime("2022-10-06 12:36:04"));
    ReflectionTestUtils.setField(rating2, "updatedAt", getDateTime("2023-09-05 12:36:04"));

    List<RatingResponse> ratings = List.of(
        RatingConverter.toRatingResponse(rating1),
        RatingConverter.toRatingResponse(rating2));
    given(ratingService.getRatingsByMovieId(movie.getId())).willReturn(ratings);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/movies/{movieId}/ratings", movie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("rating/get-ratings-by-movieid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("movieId").description("영화 고유번호")),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("평점 고유번호"),
                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("평점 내용"),
                fieldWithPath("[].rating").type(JsonFieldType.NUMBER).description("평점 점수"),
                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("업데이트 시간"),
                fieldWithPath("[].user").type(JsonFieldType.OBJECT).description("유저"),
                fieldWithPath("[].user.id").type(JsonFieldType.NUMBER).description("유저 고유번호"),
                fieldWithPath("[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                fieldWithPath("[].user.profileImage").type(JsonFieldType.STRING).description("유저 프로필 url")
            )
        ));
  }

  private LocalDateTime getDateTime(String date) {
    return LocalDateTime.parse(date, simpleDateFormat);
  }

  @Test
  @WithMockCustomAnonymousUser
  void givenUserId_whenGetRatings_thenReturnRatings() throws Exception {
    //given
    ReflectionTestUtils.setField(rating1, "createdAt", getDateTime("2022-10-05 12:36:04"));
    ReflectionTestUtils.setField(rating1, "updatedAt", getDateTime("2023-09-05 12:36:04"));
    ReflectionTestUtils.setField(rating2, "createdAt", getDateTime("2022-10-06 12:36:04"));
    ReflectionTestUtils.setField(rating2, "updatedAt", getDateTime("2023-09-05 12:36:04"));

    List<UserRatingResponse> ratings = List.of(
        RatingConverter.toUserRatingResponse(rating1),
        RatingConverter.toUserRatingResponse(rating2));
    given(ratingService.getRatingsByUserId(user.getId())).willReturn(ratings);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/accounts/{userId}/ratings", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("rating/get-ratings-by-userid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("userId").description("유저 고유번호")),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("평점 고유번호"),
                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("평점 내용"),
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

  @Test
  @WithMockCustomUser
  void givenRatingRequest_whenCreateRating_thenReturnRating() throws Exception {
    //given
    User user = User.builder()
        .id(0L)
        .email("testUser@tgmail.com")
        .password("{bcrypt}$2a$10$HvKBACAuzrvJGvpKcb8S3O7RX8uqg72U/dD5TD/3L.ps3c9Ydng6i")
        .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
        .profileImage(
            "https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
        .authority(UserRole.ROLE_MEMBER)
        .build();

    Rating newRating = Rating.builder()
        .id(0L)
        .contents(rating1.getContents())
        .rating(rating1.getRating())
        .likeCount(0L)
        .user(user)
        .movie(movie)
        .build();

    RatingCreateRequest ratingCreateRequest = RatingCreateRequest.builder()
        .contents(newRating.getContents())
        .rating(newRating.getRating())
        .build();

    given(ratingService.createRating("testUser@tgmail.com", movie.getId(), ratingCreateRequest))
        .willReturn(newRating);

    Map<String, String> request = new HashMap<>();
    request.put("contents", newRating.getContents());
    request.put("rating", String.valueOf(newRating.getRating()));

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/movies/{movieId}/ratings", movie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(document("rating/create",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }
}

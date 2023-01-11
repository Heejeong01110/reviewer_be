package org.movie.reviewer.domain.favorite.api;


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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.favorite.domain.Favorite;
import org.movie.reviewer.domain.favorite.dto.FavoriteConverter;
import org.movie.reviewer.domain.favorite.dto.response.UserFavoriteResponse;
import org.movie.reviewer.domain.favorite.service.FavoriteService;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.global.security.annotation.WithMockCustomAnonymousUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class FavoriteApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private FavoriteService favoriteService;

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
      .authority(UserRole.ROLE_MEMBER)
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
  @WithMockCustomAnonymousUser
  void givenUserId_whenGetReviewsByUserId_thenReturnReviews() throws Exception {
    //given
    List<UserFavoriteResponse> favorites = List.of(
        FavoriteConverter.toUserFavoriteResponse(favorite1),
        FavoriteConverter.toUserFavoriteResponse(favorite2));
    given(favoriteService.getFavoritesByUserId(user.getId())).willReturn(favorites);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/accounts/{userId}/favorites", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("favorite/get-favorites-by-userid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("userId").description("유저 고유번호")),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("즐겨찾기 고유번호"),
                fieldWithPath("[].movie").type(JsonFieldType.OBJECT).description("영화"),
                fieldWithPath("[].movie.id").type(JsonFieldType.NUMBER).description("영화 고유번호"),
                fieldWithPath("[].movie.title").type(JsonFieldType.STRING).description("영화명"),
                fieldWithPath("[].movie.movieImage").type(JsonFieldType.STRING).description("영화 포스터 url")
            )
        ));

  }
}

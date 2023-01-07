package org.movie.reviewer.domain.movie.api;


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
import org.movie.reviewer.domain.movie.domain.Actor;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.movie.dto.MovieConverter;
import org.movie.reviewer.domain.movie.dto.response.ActorInfo;
import org.movie.reviewer.domain.movie.dto.response.MovieDetailResponse;
import org.movie.reviewer.domain.movie.dto.response.MovieTitleResponse;
import org.movie.reviewer.domain.movie.service.MovieService;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = MovieApi.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MovieApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MovieService movieService;

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


  private List<Actor> actors = List.of(
      Actor.builder().id(0L).name("조승우").role("고니").build(),
      Actor.builder().id(1L).name("김혜수").role("정마담").build(),
      Actor.builder().id(2L).name("백윤식").role("평강장").build(),
      Actor.builder().id(3L).name("유해진").role("고광렬").build(),
      Actor.builder().id(4L).name("김응수").role("곽철용").build()
  );

  private List<ActorInfo> actorInfos = List.of(
      ActorInfo.builder().id(0L).name("조승우").role("고니").build(),
      ActorInfo.builder().id(1L).name("김혜수").role("정마담").build(),
      ActorInfo.builder().id(2L).name("백윤식").role("평강장").build(),
      ActorInfo.builder().id(3L).name("유해진").role("고광렬").build(),
      ActorInfo.builder().id(4L).name("김응수").role("곽철용").build()
  );

  private MovieTitleResponse movieTitleResponse1 = MovieConverter.toMovieTitleResponse(movie1, 5.0);

  private MovieTitleResponse movieTitleResponse2 = MovieConverter.toMovieTitleResponse(movie2, 3.0);

  private MovieDetailResponse movieDetailResponse1 = MovieDetailResponse.builder()
      .id(0L)
      .country("KR")
      .title("타짜")
      .director("최동훈")
      .movieImage(
          "https://movie-phinf.pstatic.net/20211122_91/1637564743461raGXe_JPEG/movie_image.jpg?type=m203_290_2")
      .genre("CRIME")
      .runningTime(139L)
      .summary("인생을 건 한판 승부 | 큰거 한판에 인생은 예술이 된다! | 목숨을 걸 수 없다면, 배팅하지 마라! | 꽃들의 전쟁")
      .rating(3.0)
      .actors(actorInfos)
      .build();


  @Test
  @WithMockCustomUser
  void givenValidMovieId_whenGetMovie_thenMovieInfo() throws Exception {
    //given
    given(movieService.getMovieById(movie1.getId())).willReturn(movieDetailResponse1);

    //when
    ResultActions result = mockMvc.perform(
        get("/api/v1/movies/{id}", movie1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(document("movie/get-movie",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("id").description("영화 고유번호")),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("고유번호"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("영화명"),
                fieldWithPath("movieImage").type(JsonFieldType.STRING).description("영화 포스터 사진 주소"),
                fieldWithPath("genre").type(JsonFieldType.STRING).description("영화 장르"),
                fieldWithPath("country").type(JsonFieldType.STRING).description("국가명"),
                fieldWithPath("runningTime").type(JsonFieldType.NUMBER).description("러닝 타임"),
                fieldWithPath("summary").type(JsonFieldType.STRING).description("줄거리"),
                fieldWithPath("director").type(JsonFieldType.STRING).description("감독명"),
                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점"),
                fieldWithPath("actors").type(JsonFieldType.ARRAY).description("출연진"),
                fieldWithPath("actors[].id").type(JsonFieldType.NUMBER).description("배우 고유번호"),
                fieldWithPath("actors[].name").type(JsonFieldType.STRING).description("배우명"),
                fieldWithPath("actors[].role").type(JsonFieldType.STRING).description("역할")
                )
        ));
  }


//  @Test
//  @WithMockUser(username = "movieStar@gamil.com", password = "test1234", roles = "ROLE_MEMBER")
//  void givenValidUserToken_whenGetMovies_thenMovieList() throws Exception {
//    //given
//    List<MovieTitleResponse> movies = List.of(
//        MovieConverter.toMovieTitleResponse(movie1, movieTitleResponse1.getRating()),
//        MovieConverter.toMovieTitleResponse(movie2, movieTitleResponse2.getRating()));
//
//    given(movieService.getMovieTitleList()).willReturn(movies);
//
//    //when
//    ResultActions result = mockMvc.perform(
//        get("/api/v1/movies")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//    );
//
//    //then
////    result.andExpect(status().isOk())
////        .andDo(document("movie/movies", //adoc 파일을 생성할 폴더 및 파일명
////            getDocumentRequest(), //ApiDocumentUtils
////            getDocumentResponse(), //ApiDocumentUtils
////            requestFields(),
////            responseFields(
////                fieldWithPath("")
////            )
////        ));
//
//  }

}

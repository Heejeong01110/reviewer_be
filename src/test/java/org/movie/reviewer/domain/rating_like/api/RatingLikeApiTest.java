package org.movie.reviewer.domain.rating_like.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.movie.domain.Movie;
import org.movie.reviewer.domain.rating.domain.Rating;
import org.movie.reviewer.domain.rating.dto.request.RatingCreateRequest;
import org.movie.reviewer.domain.rating.service.RatingService;
import org.movie.reviewer.domain.rating_like.domain.LikeType;
import org.movie.reviewer.domain.rating_like.domain.RatingLike;
import org.movie.reviewer.domain.rating_like.service.RatingLikeService;
import org.movie.reviewer.domain.user.domain.User;
import org.movie.reviewer.domain.user.domain.UserRole;
import org.movie.reviewer.global.security.annotation.WithMockCustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class RatingLikeApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private RatingLikeService ratingLikeService;

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

  private Rating rating = Rating.builder()
      .id(0L)
      .contents("인생영화임..")
      .rating(5.0)
      .user(user)
      .movie(movie)
      .build();

  @Test
  @WithMockCustomUser
  void givenLikeType_whenAddLike_thenReturnRatingLike() throws Exception {
    //given
    RatingLike ratingLike = RatingLike.builder()
        .id(0L)
        .likeType(LikeType.LIKE.getNum())
        .user(user)
        .rating(rating)
        .build();

    User user = User.builder()
        .id(0L)
        .email("testUser@tgmail.com")
        .password("{bcrypt}$2a$10$HvKBACAuzrvJGvpKcb8S3O7RX8uqg72U/dD5TD/3L.ps3c9Ydng6i")
        .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
        .profileImage(
            "https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
        .authority(UserRole.ROLE_MEMBER)
        .build();

    given(ratingLikeService.updateRatingLike(user.getEmail(),rating.getId(), LikeType.LIKE.getNum()))
        .willReturn(ratingLike);

    Map<String, String> request = new HashMap<>();
    request.put("like", "true");

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/rating/{ratingId}/like", rating.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(document("rating-like/undefine-to-like",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }


  @Test
  @WithMockCustomUser
  void givenLikeType_whenAddDislike_thenReturnRatingLike() throws Exception {
    //given
    RatingLike ratingLike = RatingLike.builder()
        .id(0L)
        .likeType(LikeType.DISLIKE.getNum())
        .user(user)
        .rating(rating)
        .build();

    User user = User.builder()
        .id(0L)
        .email("testUser@tgmail.com")
        .password("{bcrypt}$2a$10$HvKBACAuzrvJGvpKcb8S3O7RX8uqg72U/dD5TD/3L.ps3c9Ydng6i")
        .introduction("안녕하세요 영화를 좋아하는 영화인입니다.")
        .profileImage(
            "https://blog.kakaocdn.net/dn/bj4oa7/btqLJWFLMgd/wu4GV8PKbXdICuyW0me0zk/img.jpg")
        .authority(UserRole.ROLE_MEMBER)
        .build();

    given(ratingLikeService.updateRatingLike(user.getEmail(),rating.getId(), LikeType.UNDEFINE.getNum()))
        .willReturn(ratingLike);

    Map<String, String> request = new HashMap<>();
    request.put("dislike", "false");

    //when
    ResultActions result = mockMvc.perform(
        post("/api/v1/rating/{ratingId}/dislike", rating.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(document("rating-like/undefine-to-like",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }

}

package org.movie.reviewer.domain.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.rating.dto.response.UserRatingResponse;
import org.movie.reviewer.domain.review.dto.response.UserReviewResponse;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

  private Long id;

  private String nickname;

  private String introduction;

  private String profileImage;

  private List<UserReviewResponse> reviews;

  private List<UserRatingResponse> ratings;

}

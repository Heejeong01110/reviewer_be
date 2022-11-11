package org.movie.reviewer.domain.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.movie.reviewer.domain.rating.dto.response.UserRatingInfo;
import org.movie.reviewer.domain.review.dto.response.UserReviewInfo;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

  private Long id;

  private String nickname;

  private String introduction;

  private String profileImage;

  private List<UserReviewInfo> reviews;

  private List<UserRatingInfo> ratings;

}

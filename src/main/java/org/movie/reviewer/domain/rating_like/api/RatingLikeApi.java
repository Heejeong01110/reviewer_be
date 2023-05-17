package org.movie.reviewer.domain.rating_like.api;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.rating_like.domain.LikeType;
import org.movie.reviewer.domain.rating_like.service.RatingLikeService;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class RatingLikeApi {


  private final RatingLikeService ratingLikeService;

  @PostMapping("rating/{ratingId}/like")
  public ResponseEntity<Void> addLike(
      @PathVariable("ratingId") Long ratingId,
      @RequestBody Map<String, String> request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    LikeType likeType;
    if(request.get("like").equals("true")){
      likeType = LikeType.LIKE;
    }else if(request.get("like").equals("false")){
      likeType = LikeType.UNDEFINE;
    }else{
      throw new RuntimeException("TYPE_ERROR");
    }
    ratingLikeService.updateRatingLike(userDetails.getEmail(), ratingId, likeType.getNum());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  @PostMapping("rating/{ratingId}/dislike")
  public ResponseEntity<Void> addDislike(
      @PathVariable("ratingId") Long ratingId,
      @RequestBody Map<String, String> request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    LikeType likeType;
    if(request.get("dislike").equals("true")){
      likeType = LikeType.DISLIKE;
    }else if(request.get("dislike").equals("false")){
      likeType = LikeType.UNDEFINE;
    }else{
      throw new NotFoundException(ErrorMessage.LIKE_TYPE_NOT_FOUNDED, request.get("dislike"));
    }
    ratingLikeService.updateRatingLike(userDetails.getEmail(), ratingId, likeType.getNum());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}

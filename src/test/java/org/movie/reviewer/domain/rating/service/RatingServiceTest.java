package org.movie.reviewer.domain.rating.service;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.movie.reviewer.domain.rating.repository.RatingRepository;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

  @Spy
  @InjectMocks
  private RatingService ratingService;

  @Mock
  private RatingRepository ratingRepository;


  @Test
  void getRatingScoreByMovieId() {
    //given
    Long movieId = 0L;
    Double actual = 5.0;
    given(ratingRepository.getRatingAvgByMovieId(movieId)).willReturn(actual);

    //when
    Double expected = ratingService.getRatingScoreByMovieId(movieId);

    //then
    then(ratingRepository).should().getRatingAvgByMovieId(movieId);
    assertThat(actual, is(expected));
  }
}

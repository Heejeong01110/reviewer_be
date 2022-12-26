package org.movie.reviewer.domain.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Calendar;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.movie.reviewer.domain.user.domain.CustomUserDetails;
import org.movie.reviewer.domain.user.dto.TokenDto;
import org.movie.reviewer.global.exception.ErrorMessage;
import org.movie.reviewer.global.exception.NotFoundException;
import org.movie.reviewer.global.security.exception.JwtInvalidException;
import org.movie.reviewer.global.security.provider.JwtProvider;
import org.movie.reviewer.global.security.provider.RedisTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisTokenService {

  @Value("${jwt.refresh-reissue-day}")
  private static int refreshReissueDay;

  private final JwtProvider jwtProvider;
  private final RedisTokenProvider redisTokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  public void checkAccessTokenReissue(String accessToken) {
    try{ //만료되지 않음
      jwtProvider.validAccessToken(accessToken);
      throw new NotFoundException(ErrorMessage.USER_NOT_FOUND,"Test중 - 유효한 토큰 ");
    }catch (JwtInvalidException exception){ //검증 X
      throw new NotFoundException(ErrorMessage.USER_NOT_FOUND,"Test중 - 검증되지 않음 ");
    }catch (ExpiredJwtException exception){ //유효기간 만료
    }
  }

  public boolean isReissuePeriod(String refreshToken) {
    //만료기간이 넘지는 않았지만 현재시간+1일 보다 과거일 때
    Date now = new Date(System.currentTimeMillis());
    Date expirationDate = jwtProvider.getExpirationDateFromToken(refreshToken, "REFRESH");
    return expirationDate.after(now) && expirationDate.before(getAfterReissueDayFromCurrent(now));
  }

  @Transactional
  public String reissueRefreshToken(String email) {
    String reissueToken = jwtProvider.createRefreshToken(email);
    redisTokenProvider.saveRefreshToken(email, reissueToken);
    return reissueToken;
  }

  public boolean isNotNeedReissue(String refreshToken) {
    return jwtProvider.getExpirationDateFromToken(refreshToken, "REFRESH")
        .after(getAfterReissueDayFromCurrent(new Date(System.currentTimeMillis())));
  }

  public Date getAfterReissueDayFromCurrent(Date now){
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    calendar.add(Calendar.DATE, refreshReissueDay);
    return calendar.getTime();
  }


  @Transactional
  public String reissueAccessToken(String email) {
    CustomUserDetails details = customUserDetailsService.loadUserByUsername(email);
    String authorities = details.getAuthorities().stream().toList().get(0).toString();
    return jwtProvider.createAccessToken(email,authorities);
  }

  @Transactional
  public TokenDto reissue(TokenDto tokenDto) {
    String accessToken = tokenDto.getAccessToken();
    String refreshToken = tokenDto.getRefreshToken();

    //access토큰이 기간만 만료된 유효한 토큰인지 확인
    checkAccessTokenReissue(accessToken);

    //유효한 refresh 토큰인지 확인
    try {
      jwtProvider.validRefreshToken(refreshToken);
    } catch (ExpiredJwtException exception) { //유효기간 만료
      throw new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Test중 - 토큰 만료. 재 로그인 필요");
    }

    //저장되어있는 refresh 토큰인지 확인
    String email = jwtProvider.getUsernameFromToken(refreshToken, "REFRESH");
    if (!redisTokenProvider.isEqualTokenByUsername(email, refreshToken)) {
      throw new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Test중 - refresh token 무효");
    }

    String reissueAccessToken = accessToken;
    String reissueRefreshToken = refreshToken;

    //토큰 체크
    if (isNotNeedReissue(refreshToken)) { //여유가 많이 남았을 경우 accessToken만 재발급
      reissueAccessToken = reissueAccessToken(email);
    } else if (isReissuePeriod(refreshToken)) {//재갱신 가능 기간일 경우 refreshToken 재발급
      reissueAccessToken = reissueAccessToken(email);
      reissueRefreshToken = reissueRefreshToken(email);
    }

    return TokenDto.builder()
        .accessToken(reissueAccessToken)
        .refreshToken(reissueRefreshToken)
        .build();
  }

  @Transactional
  public void logout(TokenDto token) {
    try {
      jwtProvider.validAccessToken(token.getAccessToken());
    } catch (JwtInvalidException | ExpiredJwtException exception) { //검증 X
      throw new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Test중 - 검증되지 않음 ");
    }

    try {
      jwtProvider.validRefreshToken(token.getRefreshToken());
    } catch (JwtInvalidException | ExpiredJwtException exception) {
      throw new NotFoundException(ErrorMessage.USER_NOT_FOUND, "Test중 - 무효 토큰의 로그아웃 시도");
    }

    String email = jwtProvider.getUsernameFromToken(token.getAccessToken(), "ACCESS");
    redisTokenProvider.deleteByEmail(email);
  }

}

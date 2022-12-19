package org.movie.reviewer.global.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class RedisTokenProvider {

  private final RedisTemplate<String, String> redisTemplate;

  public void saveRefreshToken(String username, String refreshToken){
    redisTemplate.opsForValue().set(username,refreshToken);
  }

  public boolean isEqualTokenByUsername(String username, String token){
    String refreshToken = redisTemplate.opsForValue().get(username);
    return StringUtils.hasText(refreshToken) && refreshToken.equals(token);
  }


}

package org.movie.reviewer.global.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.movie.reviewer.global.security.exception.JwtInvalidException;
import org.movie.reviewer.global.security.tokens.JsonPrincipalAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonWebTokenIssuer {

  private final int ONE_SECONDS = 1000;
  private final int ONE_MINUTE = 60 * ONE_SECONDS;
  private final String KEY_ROLES = "roles";

  private final byte[] secretKeyBytes;
  private final byte[] refreshSecretKeyBytes;
  private final int expireMin;
  private final int refreshExpireMin;

  public JsonWebTokenIssuer(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.refresh-secret}") String refreshSecretKey,
      @Value("${jwt.expire-min}") int expireMin,
      @Value("${jwt.refresh-expire-min}") int refreshExpireMin) {
    this.secretKeyBytes = secretKey.getBytes();
    this.refreshSecretKeyBytes = refreshSecretKey.getBytes();
    this.expireMin = expireMin;
    this.refreshExpireMin = refreshExpireMin;
  }

  private String createToken(String email, String authority, byte[] secretKeyBytes, int expireMin) {
    Date now = new Date(System.currentTimeMillis());
    Claims claims = Jwts.claims().setSubject(email);
    claims.put(KEY_ROLES, Collections.singleton(authority));
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + ONE_MINUTE * expireMin))
        .signWith(SignatureAlgorithm.HS256, secretKeyBytes)
        .compact();
  }

  public String createAccessToken(String email, String authority) {
    return createToken(email, authority, secretKeyBytes, expireMin);
  }


  public String createAccessToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream().toList().get(0).toString();
    return createToken((String) authentication.getPrincipal(), authorities,
        secretKeyBytes, expireMin);
  }

  public String createRefreshToken(String email, String authority) {
    return createToken(email, authority, refreshSecretKeyBytes, refreshExpireMin);
  }

  public String createRefreshToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream().toList().get(0).toString();
    return createToken((String) authentication.getPrincipal(), authorities,
        refreshSecretKeyBytes, refreshExpireMin);
  }

  public boolean validToken(String jsonWebToken) {
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(secretKeyBytes)
          .parseClaimsJws(jsonWebToken)
          .getBody();
      log.info("email : " + claims.getSubject());
      return true;
    } catch (SignatureException signatureException) {
      throw new JwtInvalidException("signature key is different", signatureException);
    } catch (ExpiredJwtException expiredJwtException) {
      throw new JwtInvalidException("expired token", expiredJwtException);
    } catch (MalformedJwtException malformedJwtException) {
      throw new JwtInvalidException("malformed token", malformedJwtException);
    } catch (UnsupportedJwtException unsupportedJwtException) {
      throw new JwtInvalidException("unsupported token", unsupportedJwtException);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new JwtInvalidException("using illegal argument like null", illegalArgumentException);
    }
  }

  // 토큰을 받아 클레임을 만들고 권한정보를 빼서 시큐리티 유저객체를 만들어 Authentication 객체 반환
  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKeyBytes)
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    return new JsonPrincipalAuthenticationToken(claims.getSubject(), "", authorities);
  }
}

package org.movie.reviewer.global.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
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
public class JwtProvider {

  private static String TYPE = "type";
  private static String JWT_TYPE = "JWT";
  private final int ONE_SECONDS = 1000;
  private final int ONE_MINUTE = 60 * ONE_SECONDS;
  private final String KEY_ROLES = "roles";
  private final byte[] secretKeyBytes;
  private final byte[] refreshSecretKeyBytes;
  private final int expireMin;
  private final int refreshExpireMin;

  public JwtProvider(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.refresh-secret}") String refreshSecretKey,
      @Value("${jwt.expire-min}") int expireMin,
      @Value("${jwt.refresh-expire-min}") int refreshExpireMin) {
    this.secretKeyBytes = secretKey.getBytes();
    this.refreshSecretKeyBytes = refreshSecretKey.getBytes();
    this.expireMin = expireMin;
    this.refreshExpireMin = refreshExpireMin;
  }

  public String createAccessToken(String email, String authority) {
    Date now = new Date(System.currentTimeMillis());
    Claims claims = Jwts.claims().setSubject(email);
    claims.put(KEY_ROLES, Collections.singleton(authority));
    return Jwts.builder()
        .setHeaderParam(TYPE, JWT_TYPE)
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + ONE_MINUTE * expireMin))
        .signWith(SignatureAlgorithm.HS256, secretKeyBytes)
        .compact();
  }


  public String createRefreshToken(String username) {
    Date now = new Date(System.currentTimeMillis());
    Claims claims = Jwts.claims().setSubject(username);
    return Jwts.builder()
        .setHeaderParam(TYPE, JWT_TYPE)
        .setClaims(claims)
        .setExpiration(new Date(now.getTime() + ONE_MINUTE * refreshExpireMin))
        .signWith(SignatureAlgorithm.HS256, refreshSecretKeyBytes)
        .compact();
  }


  public boolean validAccessToken(String token) {
    return validToken(token, secretKeyBytes);
  }


  public boolean validRefreshToken(String token) {
    return validToken(token, refreshSecretKeyBytes);
  }

  public boolean validToken(String token, byte[] key) {
    try {
      Jwts.parser()
          .setSigningKey(key)
          .parseClaimsJws(token)
          .getBody();
      return true;
    } catch (SignatureException signatureException) { //토큰 오류 시 500에러가 아니라 unauthorization 오류가 나야함
      throw new JwtInvalidException("signature key is different", signatureException);
    }
    //ExpiredJwtException은 custom 하지 않고 바로 리턴해서 access 토큰의 만료여부 체크
    catch (MalformedJwtException malformedJwtException) {
      throw new JwtInvalidException("malformed token", malformedJwtException);
    } catch (UnsupportedJwtException unsupportedJwtException) {
      throw new JwtInvalidException("unsupported token", unsupportedJwtException);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new JwtInvalidException("using illegal argument like null", illegalArgumentException);
    }
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKeyBytes)
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
            .map(SimpleGrantedAuthority::new).toList();

    return new JsonPrincipalAuthenticationToken(claims.getSubject(), "", authorities);
  }

  public String getAuthorities(String token){
    Claims claims = Jwts.parser()
        .setSigningKey(secretKeyBytes)
        .parseClaimsJws(token)
        .getBody();

    List<SimpleGrantedAuthority> authorities = Arrays.stream(
            claims.get(KEY_ROLES).toString().split(","))
        .map(SimpleGrantedAuthority::new).toList();
    return authorities.get(0).toString();

  }

  public Date getExpirationDateFromToken(String token, String keyType) {
    byte[] secret = keyType.equals("ACCESS") ? secretKeyBytes
        : keyType.equals("REFRESH") ? refreshSecretKeyBytes
            : null;
    if (secret == null) {
      throw new NullPointerException("not found key");
    }
    return getClaimFromToken(token, secret, Claims::getExpiration);
  }

  public String getUsernameFromToken(String accessToken, String keyType) {
    byte[] secret = keyType.equals("ACCESS") ? secretKeyBytes
        : keyType.equals("REFRESH") ? refreshSecretKeyBytes
            : null;
    if (secret == null) {
      throw new NullPointerException("not found key");
    }
    return getClaimFromToken(accessToken, secret, Claims::getSubject);
  }

  private <T> T getClaimFromToken(String accessToken, byte[] secretKey,
      Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(accessToken, secretKey);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token, byte[] secretKey) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

}

package org.movie.reviewer.global.security.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacSigner;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JwtManager {

  public Jwt createJwt(String id){
    return null;
  }


  private String createPayload(String id){
    return null;
  }


  private long getIssueAt(){
    return System.currentTimeMillis();
  }
}

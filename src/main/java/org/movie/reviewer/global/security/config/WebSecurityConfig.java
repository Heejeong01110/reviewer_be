package org.movie.reviewer.global.security.config;

import org.movie.reviewer.global.security.provider.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final String ROLE_ADMIN = "ADMIN";
  private final String ROLE_NORMAL = "NORMAL";

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public WebSecurityConfig(
      AuthenticationManagerBuilder authenticationManagerBuilder,
      JwtAuthenticationProvider jsonWebTokenProvider) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.authenticationManagerBuilder.authenticationProvider(jsonWebTokenProvider);
  }

  //시큐리티 룰 무시 url
  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers("/h2-console/**")
        .antMatchers("/assets/**");
  }

  //시큐리티 규칙
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable() //csrf 설정
        .sessionManagement() //세션 사용X
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and() //h2-console 사용 설정
        .headers()
        .frameOptions()
        .sameOrigin()
    .and() //jwt 인증필터 설정
        .apply(new JwtSecurityConfig(authenticationManagerBuilder.getOrBuild()))
    .and()
        .authorizeRequests()
        .antMatchers("/accounts/**").hasAnyRole(ROLE_ADMIN, ROLE_NORMAL)
        .anyRequest().permitAll();

  }


//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }

}

package org.movie.reviewer.global.security.config;

import org.movie.reviewer.domain.auth.service.PrincipalDetailsService;
import org.movie.reviewer.global.security.filter.JsonPrincipalAuthenticationFilter;
import org.movie.reviewer.global.security.filter.JwtAuthorizationFilter;
import org.movie.reviewer.global.security.handler.CustomAccessDeniedHandler;
import org.movie.reviewer.global.security.handler.CustomAuthenticationEntryPoint;
import org.movie.reviewer.global.security.handler.JwtSuccessHandler;
import org.movie.reviewer.global.security.provider.JsonPrincipalAuthenticationProvider;
import org.movie.reviewer.global.security.utils.JsonWebTokenIssuer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final RequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher(
      "/api/v1/login", "POST");
  private final String ROLE_ADMIN = "ADMIN";
  private final String ROLE_NORMAL = "NORMAL";
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;
  private final CustomAccessDeniedHandler accessDeniedHandler;
  private final JsonWebTokenIssuer jwtIssuer;
  private final PrincipalDetailsService detailsService;
  private final JsonPrincipalAuthenticationProvider jsonPrincipalAuthenticationProvider;

  public WebSecurityConfig(
      CustomAuthenticationEntryPoint authenticationEntryPoint,
      CustomAccessDeniedHandler accessDeniedHandler,
      JsonWebTokenIssuer jwtIssuer,
      PrincipalDetailsService detailsService,
      JsonPrincipalAuthenticationProvider jsonPrincipalAuthenticationProvider) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
    this.jwtIssuer = jwtIssuer;
    this.detailsService = detailsService;
    this.jsonPrincipalAuthenticationProvider = jsonPrincipalAuthenticationProvider;
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
    JsonPrincipalAuthenticationFilter jsonPrincipalAuthenticationFilter = new JsonPrincipalAuthenticationFilter(
        LOGIN_REQUEST_MATCHER, authenticationManagerBean(), jwtIssuer);
    jsonPrincipalAuthenticationFilter
        .setAuthenticationSuccessHandler(authenticationSuccessHandler());

    http
        .csrf().disable() //csrf 설정
        .headers() //h2-console 사용 설정
        .frameOptions()
        .sameOrigin()
        .and()
        .sessionManagement() //세션 사용X
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling() //에러 핸들러 설정
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler)
        .and()
        // 인증 필터 -> AbstractAuthenticationProcessingFilter 구현체
        //addFilterAt() 지정된 필터 순서에 커스텀 필터 추가. 지정된 필터보다 커스텀 필터가 먼저 실행됨
        .addFilterAt(jsonPrincipalAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        //인가 필터 -> OncePerRequestFilter 구현체
//        .addFilter(jwtAuthorizationFilter())
        .authorizeRequests()
//        .antMatchers("/accounts/**").hasAnyRole(ROLE_ADMIN, ROLE_NORMAL)
        .anyRequest().permitAll();

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(jsonPrincipalAuthenticationProvider);
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
    return new JwtAuthorizationFilter(detailsService, jwtIssuer, authenticationManager());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtSuccessHandler(jwtIssuer);
  }

}

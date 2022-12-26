package org.movie.reviewer.global.config;

import org.movie.reviewer.domain.user.service.CustomUserDetailsService;
import org.movie.reviewer.global.security.filter.CustomAuthenticationFilter;
import org.movie.reviewer.global.security.filter.JwtAuthorizationFilter;
import org.movie.reviewer.global.security.handler.CustomAccessDeniedHandler;
import org.movie.reviewer.global.security.handler.JwtAuthenticationFailHandler;
import org.movie.reviewer.global.security.handler.JwtAuthenticationSuccessHandler;
import org.movie.reviewer.global.security.provider.CustomAuthenticationProvider;
import org.movie.reviewer.global.security.provider.JwtProvider;
import org.movie.reviewer.global.security.provider.RedisTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
  private final JwtProvider jwtProvider;
  private final RedisTokenProvider redisTokenProvider;
  private final CustomUserDetailsService userDetailsService;


  public WebSecurityConfig(
      JwtProvider jwtProvider,
      RedisTokenProvider redisTokenProvider,
      CustomUserDetailsService userDetailsService) {
    this.jwtProvider = jwtProvider;
    this.redisTokenProvider = redisTokenProvider;
    this.userDetailsService = userDetailsService;
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
    // 인증 필터 -> AbstractAuthenticationProcessingFilter 구현체
    CustomAuthenticationFilter customAuthenticationFilter =
        new CustomAuthenticationFilter(
            LOGIN_REQUEST_MATCHER,
            authenticationManagerBean(),
            jwtProvider,
            passwordEncoder(),
            redisTokenProvider);
    customAuthenticationFilter
        .setAuthenticationSuccessHandler(authenticationSuccessHandler());
    customAuthenticationFilter
        .setAuthenticationFailureHandler(authenticationFailureHandler());

    http
        .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
        .csrf().disable() //csrf 설정
        .headers() //h2-console 사용 설정
        .frameOptions()
        .sameOrigin()
        .and()
        .sessionManagement() //세션 사용X
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling() //에러 핸들러 설정
//        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        // 인증 필터 -> AbstractAuthenticationProcessingFilter 구현체
        .addFilterAt(customAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class) //지정된 필터 순서에 커스텀 필터 추가. 지정된 필터보다 커스텀 필터가 먼저 실행됨
        .authorizeRequests()
//        .antMatchers("/accounts/**").hasAnyRole(ROLE_ADMIN, ROLE_NORMAL)
        .anyRequest().permitAll();

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }


  @Bean //인가 필터 -> OncePerRequestFilter 구현체
  public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
    return new JwtAuthorizationFilter(jwtProvider, authenticationManager());
  }

  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new CustomAuthenticationProvider(userDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new JwtAuthenticationFailHandler();
  }

//  @Bean
//  public AuthenticationEntryPoint authenticationEntryPoint() {
//    return new CustomAuthenticationEntryPoint();
//  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

}

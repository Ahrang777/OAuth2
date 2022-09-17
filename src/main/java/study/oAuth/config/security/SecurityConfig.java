package study.oAuth.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.oAuth.config.security.handler.CustomSimpleUrlAuthenticationFailureHandler;
import study.oAuth.config.security.handler.CustomSimpleUrlAuthenticationSuccessHandler;
import study.oAuth.config.security.token.CustomAuthenticationEntryPoint;
import study.oAuth.config.security.token.CustomOncePerRequestFilter;
import study.oAuth.domain.service.auth.CustomTokenProviderService;
import study.oAuth.domain.repository.CustomAuthorizationRequestRepository;
import study.oAuth.domain.service.auth.CustomDefaultOAuth2UserService;
import study.oAuth.domain.service.auth.CustomUserDetailsService;

@Configuration
@EnableWebSecurity // spring security 설정을 활성화시켜주는 어노테이션
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final CustomTokenProviderService customTokenProviderService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomDefaultOAuth2UserService customOAuth2UserService;
    private final CustomSimpleUrlAuthenticationSuccessHandler customSimpleUrlAuthenticationSuccessHandler;
    private final CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final CustomOncePerRequestFilter customOncePerRequestFilter;


//    @Bean
//    public CustomOncePerRequestFilter customOncePerRequestFilter() {
//        return new CustomOncePerRequestFilter();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                .authorizeRequests()
                .antMatchers("/", "/error","/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                .permitAll()
                .antMatchers("/login/**","/auth/**", "/oauth2/**")
                .permitAll()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/blog/**")
                .permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization/**")
                .authorizationRequestRepository(customAuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/**")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(customSimpleUrlAuthenticationSuccessHandler)
                .failureHandler(customSimpleUrlAuthenticationFailureHandler);

//        http.addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(customOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    
    
    
    
    
    
    // 제외하고 생각
//    @Bean
//    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
//        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
//                new DefaultAuthorizationCodeTokenResponseClient();
//
//        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
//                new OAuth2AccessTokenResponseHttpMessageConverter();
//        tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter());
//        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
//                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
//        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//
//        accessTokenResponseClient.setRestOperations(restTemplate);
//        return accessTokenResponseClient;
//    }
}

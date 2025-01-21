package com.example.hanbit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.example.hanbit.filter.JwtFilter;
import com.example.hanbit.security.AuthEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화
            .authorizeRequests()
            	.antMatchers(HttpMethod.GET,
            			"/test","/home","/intro","/location","/reviews","/upload/**","/places/**"
            			).permitAll()
                .antMatchers(HttpMethod.POST,
                		"/test","/home","/signup","/login","/intro","/api/email/send-verification-email","/api/email/verify-code","/places/**"
                		).permitAll() 
                .antMatchers(HttpMethod.PUT,
                		"/test","/home"
                		).permitAll()
                .antMatchers(HttpMethod.DELETE,
                		"/test","/home"
                		).permitAll()
//                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // 나머지 요청은 인증이 필요함
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint) // 인증되지 않은 접근 시 진입점 설정
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter 전에 JWT 필터 추가
        	.cors();

        return http.build();
    }
    
    @Bean
	public CorsConfigurationSource configurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("http://localhost:5173"); // 특정 오리진을 허용(예: 로컬호스트의 프론트엔드)
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
		source.registerCorsConfiguration("/**", config);//모든 경로에 대해 CORS설정 적용
		return source;
	}
    
 	@Bean
 	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
 		return authenticationConfiguration.getAuthenticationManager(); // AuthenticationManager를 AuthenticationConfiguration에서 가져옴
 	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 사용
    }
}

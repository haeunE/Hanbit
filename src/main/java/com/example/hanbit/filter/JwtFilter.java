package com.example.hanbit.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.hanbit.service.JwtService;


@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (jwt != null) {
            // JWT 토큰이 유효한 경우
            if (jwtService.isTokenValid(jwt)) {
                // 로그인 상태에서 로그인 페이지에 접근하려는 경우
                if (request.getRequestURI().equals("/login") || request.getRequestURI().startsWith("/login")) {
                    response.sendRedirect("/home"); // 홈 페이지로 리다이렉트
                    return; // 더 이상 필터 체인 진행하지 않음
                }

                // 유효한 JWT 토큰이 있으면 인증 정보를 설정
                String username = jwtService.getAuthUser(request);
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        // 필터 체인 진행
        filterChain.doFilter(request, response);
    }
}


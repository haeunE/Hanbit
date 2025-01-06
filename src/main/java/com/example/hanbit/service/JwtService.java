package com.example.hanbit.service;

import java.security.Key;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	//토큰 만료 시간 설정
	static final long EXPIRATIONTIME = 2 * 60 * 60 * 1000;
	// 해더에 사용할 접두어 설정
	static final String PRIFIX = "Bearer ";
	//암호화 키 생성
	static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	//로그인 아이디를 이용해 jwt토큰 생성 후 리턴 시켜주는 메서드
	public String getToken(String username) {
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATIONTIME)) //현재 시간 + 24시간 동안 토큰유지
				.signWith(key)
				.compact();
		return token;
	}
	
	//요청 객체에서 헤더에 jwt를 추출하고 그 안에 있는 username을 리턴
	public String getAuthUser(HttpServletRequest request) {
	    String token = request.getHeader(HttpHeaders.AUTHORIZATION);  // HttpServletRequest에서 Authorization 헤더 추출
	    if (token != null) {
	        System.out.println("tocken :"+token);
	        try {
	            // JWT에서 사용자 정보를 추출
	            String user = Jwts.parserBuilder()
	                    .setSigningKey(key)  // JWT 서명 검증을 위한 키
	                    .build()
	                    .parseClaimsJws(token)  // 토큰을 파싱하여 Claims 추출
	                    .getBody()
	                    .getSubject();  // 사용자 이름 추출
	            
	            if (user != null) {
	                return user;
	            }
	        } catch (JwtException | IllegalArgumentException e) {
	            // 예외 처리: 토큰이 유효하지 않거나 파싱에 실패한 경우
	            return null;
	        }
	    }
	    return null;
	}
}

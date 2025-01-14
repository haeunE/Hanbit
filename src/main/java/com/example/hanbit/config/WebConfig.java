package com.example.hanbit.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	// CORS(Cross-Origin Resource Sharing) 설정 메서드
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 모든 경로에 대해 CORS 허용 설정
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173") 
			.allowedMethods("GET","POST","PUT","DELETE")
			.allowedHeaders("*"); // 허용할 HTTP 메서드 목록
	}
	
	
	// 정적 리소스 핸들러 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/"); // 실제 경로
    }
    
    @Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
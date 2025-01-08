package com.example.hanbit.filter;

import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import com.example.hanbit.security.UserDetailsServiceImpl;
import com.example.hanbit.service.JwtService;
import com.example.hanbit.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {
	private final UserService userService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void cleanupLeavers() {
        userService.deleteOldLeavers();
    }
}

package com.example.hanbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.hanbit.service.EmailService;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class AuthController {

    @Autowired
    private EmailService emailService;

    private Map<String, String> emailVerificationCodes = new HashMap<>();

    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> request) {
    	String email = request.get("email");
    	System.out.println(email);
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendVerificationEmail(email, verificationCode);

        // 인증 코드 저장 (일시적으로 메모리)
        emailVerificationCodes.put(email, verificationCode);

        return ResponseEntity.ok("인증 코드가 이메일로 발송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
    	String email = request.get("email");
    	String code = request.get("code");
        String savedCode = emailVerificationCodes.get(email);

        if (savedCode != null && savedCode.equals(code)) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 코드가 잘못되었습니다.");
        }
    }
}
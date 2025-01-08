package com.example.hanbit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanbit.service.JwtService;
import com.example.hanbit.domain.RoleType;
import com.example.hanbit.domain.User;
import com.example.hanbit.dto.JwtResponse;
import com.example.hanbit.dto.LoginForm;
import com.example.hanbit.dto.SignUpForm;
import com.example.hanbit.dto.UpdateForm;
import com.example.hanbit.repository.UserRepository;
import com.example.hanbit.security.UserDetailsImpl;
import com.example.hanbit.security.UserDetailsServiceImpl;
import com.example.hanbit.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult) {
	    // 유효성 검사 실패 시 오류 메시지를 반환
	    if (bindingResult.hasErrors()) {
	        StringBuilder errorMessages = new StringBuilder();
	        bindingResult.getAllErrors().forEach(error -> {
	            errorMessages.append(error.getDefaultMessage()).append("\n");
	        });
	        return ResponseEntity.badRequest().body(errorMessages.toString());
	    }
	    
	    try {
	    	// 유효성 검사 통과 시 회원가입 로직 처리
		    userService.saveUser(signUpForm);
	        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
	    } catch (Exception e) { 
	        // 예외 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
	    }
	}
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
	    }
	    try {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(loginForm.getLogin_username(), loginForm.getLogin_password())
	        );
	        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(loginForm.getLogin_username());
	        User user = userDetails.getUser();
	        if (user.getRoleType().equals(RoleType.LEAVER))
	        	userService.updateRoleToMember(loginForm.getLogin_username());
	        String token = jwtService.getToken(loginForm.getLogin_username());
	        System.out.println(user);
	        JwtResponse response = new JwtResponse(
	                token,
	                user.getUsername(),
	                user.getName(), // 사용자 이름
	                user.getEmail(),
	                user.getTel(), // 비상 연락처
	                user.isForeignYN() // 외국인 여부
	            );

	        return ResponseEntity.ok(response);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	    }
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestBody UpdateForm updatedUser, HttpServletRequest request) {
	    // JWT에서 사용자 정보를 추출
		System.out.println(updatedUser);
	    String username = jwtService.getAuthUser(request);  // 이제 HttpServletRequest를 사용하여 사용자 정보 추출
	    
	    if (username != null) {

	        // 사용자 정보를 업데이트하는 서비스 호출
	        User updatedUserInfo = userService.updateUser(username, updatedUser);
	        
	        System.out.println("updatedUserInfo"+updatedUserInfo);
	        
	        return ResponseEntity.ok("업데이트 완료");  // 성공적으로 업데이트된 정보 반환
	    } else {
	        // 토큰이 유효하지 않거나 사용자가 인증되지 않은 경우
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원정보를 찾지 못했습니다.");
	    }
	}
	@PostMapping("/deleteUser")
	public ResponseEntity<?> deleteUser(HttpServletRequest request) {
	    String username = jwtService.getAuthUser(request);
	    if (username != null) {
	        // 사용자 상태를 LEAVER로 변경
	        userService.requestLeave(username);
	        
	        // 3일 이상 지난 사용자들을 삭제하는 메서드를 호출
	        userService.deleteOldLeavers();
	        
	        return ResponseEntity.ok("휴면 계정처리 완료 및 3일 지난 사용자 삭제 완료");
	    } else {
	        // 토큰이 유효하지 않거나 사용자가 인증되지 않은 경우
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원정보를 찾지 못했습니다.");
	    }
	}

	@DeleteMapping("/cleanup")
	public String cleanupLeavers() {
	    userService.deleteOldLeavers();
	    return "3일이 지나 사용자 삭제가 완료되었습니다.";
	}
	
	@PostMapping("/usercheck")
	public ResponseEntity<?> checkPassword(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
	    String username = jwtService.getAuthUser(httpRequest);
	    if (username == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
	    }

	    String inputPassword = request.get("password");

	    User user = userRepository.findByUsername(username)
	        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

	    // 비밀번호 검증 (예: Bcrypt 사용)
	    if (passwordEncoder.matches(inputPassword, user.getPassword())) {
	        return ResponseEntity.ok("비밀번호 확인 성공");
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증에 실패했습니다.");
	    }
	}

}

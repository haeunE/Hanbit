package com.example.hanbit.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hanbit.domain.RoleType;
import com.example.hanbit.domain.User;
import com.example.hanbit.dto.LoginForm;
import com.example.hanbit.dto.SignUpForm;
import com.example.hanbit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;  // UserDetailsService 주입

	
	public void checkUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByTel(user.getTel()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }
    }
	
	public void saveUser(SignUpForm signUpForm) {
		User user = new User();
		BeanUtils.copyProperties(signUpForm, user);
	    user.setPassword(passwordEncoder.encode(signUpForm.getPassword())); // 비밀번호 암호화
	    
	 // 사용자 존재 여부 체크
	    checkUser(user);

	    // 사용자 저장
	    userRepository.save(user);
	}
	public User updateUser(String username, User userDetails) {
        Optional<User> existingUserOptional = userRepository.findByUsername(username);
        System.out.println("===========================");
        System.out.println(existingUserOptional);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            System.out.println("===========================");
            System.out.println(existingUser);
            // 사용자 정보만 수정
            if (userDetails.getName() != null) {
                existingUser.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getTel() != null) {
                existingUser.setTel(userDetails.getTel());
            }
            if (userDetails.isForeignYN() != existingUser.isForeignYN()) {
                existingUser.setForeignYN(userDetails.isForeignYN());
            }
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                existingUser.setPassword(userDetails.getPassword());  // 비밀번호 변경
            }

            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }
	
    
}

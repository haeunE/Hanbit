package com.example.hanbit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hanbit.domain.RoleType;
import com.example.hanbit.domain.User;
import com.example.hanbit.dto.SignUpForm;
import com.example.hanbit.dto.UpdateForm;
import com.example.hanbit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Long findid(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		Long id = user.get().getId();
		return id;
	}

	
	public void checkUser(SignUpForm user) {
	    // 중복된 아이디가 있는지 확인
	    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
	    }
	    // 중복된 이메일이 있는지 확인
	    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
	    }
	    // 중복된 전화번호가 있는지 확인
	    if (userRepository.findByTel(user.getTel()).isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
	    }
	}

	public void saveUser(SignUpForm signUpForm) {
		
	    // 사용자 존재 여부 체크
	    checkUser(signUpForm);
	    System.out.println("SignUpForm email: " + signUpForm.getEmail());
	    // 새 User 객체 생성 및 필드 복사
	    User user = new User();
	    BeanUtils.copyProperties(signUpForm, user);
	    
	    // 비밀번호 암호화 후 설정
	    user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

	    // 사용자 저장
	    userRepository.save(user);
	}
	public User updateUser(String username, UpdateForm userDetails) {
        Optional<User> existingUserOptional = userRepository.findByUsername(username);
        System.out.println("===========================");
        System.out.println(existingUserOptional);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            System.out.println("===========================");
            System.out.println(existingUser);
            
            
            if (userDetails.getTel() != null) {
                existingUser.setTel(userDetails.getTel());
            }
            if (userDetails.isForeignYN() != existingUser.isForeignYN()) {
                existingUser.setForeignYN(userDetails.isForeignYN());
            }
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()) );  // 비밀번호 변경
            }

            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }
	@Transactional
    public void requestLeave(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setRoleType(RoleType.LEAVER);
        userRepository.save(user);
    }

	@Transactional
	public void deleteOldLeavers() {
	    // 현재 시간 기준으로 3일이 지난 사용자 삭제
	    LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
	    List<User> oldLeavers = userRepository.findAllByRoleTypeAndUpdateDateBefore(RoleType.LEAVER, threeDaysAgo);

	    // 삭제할 사용자들을 처리
	    for (User user : oldLeavers) {
	        userRepository.delete(user);
	    }
	}
	@Transactional
    public void updateRoleToMember(String username) {
		System.out.println("Member로 전환" + username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 사용자 role을 MEMBER로 변경
        user.setRoleType(RoleType.MEMBER);

        // 변경된 사용자 저장
        userRepository.save(user);
    }
	
	
    
}
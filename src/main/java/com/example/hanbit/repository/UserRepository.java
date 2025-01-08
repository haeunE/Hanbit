package com.example.hanbit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hanbit.domain.RoleType;
import com.example.hanbit.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByTel(String tel);
	List<User> findAllByRoleTypeAndUpdateDateBefore(RoleType roleType, LocalDateTime dateTime);
}

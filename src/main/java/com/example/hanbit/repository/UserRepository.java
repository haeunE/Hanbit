package com.example.hanbit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hanbit.domain.RoleType;
import com.example.hanbit.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	@Query("SELECT u FROM User u WHERE u.roleType = :roleType")
    User findByRoleType(@Param("roleType") RoleType roleType);
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByTel(String tel);
	List<User> findAllByRoleTypeAndUpdateDateBefore(RoleType roleType, LocalDateTime dateTime);
}

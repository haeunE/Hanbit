package com.example.hanbit.filter;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.hanbit.domain.RoleType;
import com.example.hanbit.domain.User;
import com.example.hanbit.repository.UserRepository;


@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.default.username}")
    private String defaultAdminUsername;

    @Value("${admin.default.password}")
    private String defaultAdminPassword;

    @Value("${spring.mail.username}")
    private String defaultAdminEmail;
    
    @Transactional
    @Override
    public void run(String... args) throws Exception {
    	System.out.println(userRepository.findByRoleType(RoleType.ADMIN));
        if (userRepository.findByRoleType(RoleType.ADMIN) == null) {
            User admin = new User();
            admin.setUsername(defaultAdminUsername);
            admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
            admin.setName(defaultAdminUsername);
            admin.setEmail(defaultAdminEmail);
            admin.setTel("02-000-0000");
            admin.setForeignYN(false);
            admin.setRoleType(RoleType.ADMIN);

            userRepository.save(admin);
            System.out.println("Default admin account created: username=" + defaultAdminUsername);
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}
package se2.group6.librarymanagement.config.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.model.enums.Role;
import se2.group6.librarymanagement.repository.UserRepository;

@Configuration
public class AdminInitializer {
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findUserByUserName("admin@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("12345678"));
                admin.setRole(Role.LIBRARY_PATRON);
                userRepository.save(admin);
                System.out.println("Admin created! admin@gmail.com / 12345678");
            }
        };
    }
}

package se2.group6.librarymanagement.config;

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
            if (userRepository.findUserByUserName("admin1").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin1");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRole(Role.LIBRARIAN);
                userRepository.save(admin);
                System.out.println("Admin created! admin1 / 123456");
            }
        };
    }
}

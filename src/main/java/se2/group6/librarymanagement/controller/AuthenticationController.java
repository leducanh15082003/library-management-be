package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.config.security.JwtTokenProvider;
import se2.group6.librarymanagement.dto.ForgotPasswordRequestDTO;
import se2.group6.librarymanagement.dto.ForgotPasswordResponseDTO;
import se2.group6.librarymanagement.dto.JwtAuthenticationResponseDTO;
import se2.group6.librarymanagement.dto.LoginRequestDTO;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.model.enums.Role;
import se2.group6.librarymanagement.repository.UserRepository;
import se2.group6.librarymanagement.service.UserService;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        String jwt = jwtTokenProvider.generateToken(authentication);
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
        return ResponseEntity.ok(new JwtAuthenticationResponseDTO(jwt, role));
    }

    @PostMapping("/register")
    public User createNewUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = new User();
        user.setUserName(loginRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(loginRequestDTO.getPassword()));
        user.setRole(Role.LIBRARY_PATRON);
        return userService.saveUser(user);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        Optional<User> userOptional = userRepository.findUserByUserName(request.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        User user = userOptional.get();
        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(new ForgotPasswordResponseDTO(newPassword));
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}

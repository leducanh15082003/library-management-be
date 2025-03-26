package se2.group6.librarymanagement.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.dto.*;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.model.enums.Role;
import se2.group6.librarymanagement.repository.UserRepository;
import se2.group6.librarymanagement.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute LoginRequestDTO loginRequest, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/home";
        } catch (Exception e) {
            System.out.println("Đăng nhập thất bại: " + e.getMessage());
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupFormDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("signupForm") SignupFormDTO signupForm, BindingResult bindingResult, Model model) {
        if (!signupForm.getPassword().equals(signupForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.signupForm", "Mật khẩu xác nhận không khớp");
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        User user = new User();
        user.setUserName(signupForm.getUsername());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(Role.LIBRARY_PATRON);
        userService.saveUser(user);

        return "redirect:/auth/login?registered;";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordForm(@RequestParam("username") String username, Model model) {
        Optional<User> userOptional = userRepository.findUserByUserName(username);
        if (!userOptional.isPresent()) {
            model.addAttribute("error", "User không tồn tại!");
            return "forgot-password";
        }

        User user = userOptional.get();
        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("newPassword", newPassword);
        return "forgot-password";
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}

package se2.group6.librarymanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import se2.group6.librarymanagement.config.security.CustomUserDetailsService;
import se2.group6.librarymanagement.handler.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityCfg {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Các URL công khai
                        .requestMatchers("/auth/**", "/images/**", "/css/**", "/js/**")
                        .permitAll()

                        // Role.LIBRARY_PATRON chỉ được truy cập URL bình thường
                        .requestMatchers("/admin/**").hasRole("LIBRARIAN") // Chỉ Role.LIBRARIAN mới vào /admin/*
                        .requestMatchers("/**").hasRole("LIBRARY_PATRON") // Role.LIBRARY_PATRON chỉ vào các URL bình thường

                        .anyRequest().authenticated() // Các yêu cầu còn lại phải xác thực
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }

    // Custom Access Denied Handler
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            // Redirect to a custom page or handle the denial in another way
            response.sendRedirect("/access-denied");
        };
    }
}

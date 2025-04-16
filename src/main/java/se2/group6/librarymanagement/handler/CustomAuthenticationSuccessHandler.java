package se2.group6.librarymanagement.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.model.User;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
//        if (authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"))) {
//            response.sendRedirect("/admin/dashboard");
//        } else if (authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARY_PATRON"))) {
//            response.sendRedirect("/home");
//        } else if (!user.isProfileCompleted()) {
//            response.sendRedirect("/profile");
//        } else {
//            response.sendRedirect("/");
//        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"))) {
            response.sendRedirect("/admin/dashboard");
        }
        else if (!user.isProfileCompleted()) {
            response.sendRedirect("/profile");
        }
        else {
            response.sendRedirect("/home");
        }
    }
}

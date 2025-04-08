package se2.group6.librarymanagement.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Kiểm tra vai trò của người dùng và chuyển hướng đến trang tương ứng
        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_LIBRARIAN"))) {
            response.sendRedirect("/admin/user-management");  // Role LIBRARIAN đi đến trang admin
        } else if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_LIBRARY_PATRON"))) {
            response.sendRedirect("/home");  // Role LIBRARY_PATRON đi đến trang home
        } else {
            response.sendRedirect("/");  // Mặc định, chuyển hướng đến trang chủ
        }
    }
}

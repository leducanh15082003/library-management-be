package se2.group6.librarymanagement.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.service.UserService;

import java.util.Arrays;
import java.util.List;

@Component
public class ProfileCompletionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    // List of paths that should be accessible without a completed profile
    private final List<String> allowedPaths = Arrays.asList(
            "/profile/edit",
            "/profile",
            "/auth/login",
            "/auth/signup",
            "/auth/forgot-password",
            "/logout",
            "/css/",
            "/js/",
            "/images/"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the request path is in the allowed paths list
        String requestPath = request.getRequestURI();

        // Allow access to static resources and excluded paths
        for (String allowedPath : allowedPaths) {
            if (requestPath.startsWith(allowedPath)) {
                return true;
            }
        }

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If not authenticated or anonymous user, allow the request to proceed
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return true;
        }

        // Check if user is ROLE_LIBRARIAN (admin), they should have access to everything
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"))) {
            return true;
        }

        // For authenticated users, check if profile is completed
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userDetails.getUser();

            // If profile is not completed, redirect to profile page with a message
            if (!currentUser.isProfileCompleted()) {
                // Store message in session
                HttpSession session = request.getSession();
                session.setAttribute("profileCompletionMessage", "Vui lòng cập nhật đầy đủ thông tin cá nhân của bạn trước khi truy cập các trang khác.");

                response.sendRedirect("/profile/edit");
                return false;
            }
        }

        // Allow the request to proceed
        return true;
    }
} 
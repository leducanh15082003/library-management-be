package se2.group6.librarymanagement.controller.user;

import com.cloudinary.Cloudinary;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public String showProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("isEdit", false);
        return "profile/profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, HttpServletRequest request) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        
        // Check for profile completion message in session
        HttpSession session = request.getSession();
        String profileMessage = (String) session.getAttribute("profileCompletionMessage");
        if (profileMessage != null) {
            model.addAttribute("profileCompletionMessage", profileMessage);
            // Remove the message from session after retrieving it
            session.removeAttribute("profileCompletionMessage");
        }
        
        return "profile/profile";
    }

    @PostMapping("/save")
    public String saveProfile(@RequestParam("profileImage") MultipartFile profileImage,
                              @ModelAttribute User user,
                              RedirectAttributes redirectAttributes) {
        try {
            // Ensure the profile is marked as completed
            user.setProfileCompleted(true);
            
            // Update the user profile
            userService.updateUser(user, profileImage);
            
            // Refresh the authentication context with the updated user
            refreshAuthenticationContext();
            
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/profile";
    }
    
    /**
     * Refreshes the authentication context with the updated user information
     */
    private void refreshAuthenticationContext() {
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Get the updated user
        User updatedUser = userService.getCurrentUser();
        
        // Create a new CustomUserDetails with the updated user
        CustomUserDetails newPrincipal = new CustomUserDetails(updatedUser);
        
        // Create a new authentication token with the updated principal
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                newPrincipal, 
                auth.getCredentials(),
                auth.getAuthorities()
        );
        
        // Update the security context with the new authentication
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}

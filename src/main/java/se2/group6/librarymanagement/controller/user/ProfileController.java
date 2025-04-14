package se2.group6.librarymanagement.controller.user;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String editProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        return "profile/profile";
    }

    @PostMapping("/save")
    public String saveProfile(@RequestParam("profileImage") MultipartFile profileImage,
                              @ModelAttribute User user,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user, profileImage);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}

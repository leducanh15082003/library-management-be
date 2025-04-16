package se2.group6.librarymanagement.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se2.group6.librarymanagement.dto.UserDTO;
import se2.group6.librarymanagement.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/user-management")
    public String userManagementPage(Model model) {
        model.addAttribute("users", userService.getAllUserDTOs());
        for(UserDTO user : userService.getAllUserDTOs()) {
            System.out.println(user);
        }
        return "admin/user-management";
    }

    @PostMapping("/user-management/deleteUsers")
    public String deleteUserManagement(@RequestParam("ids") List<Long> ids) {
        for(Long id: ids) {
            userService.deleteUserById(id);
        }
        return "redirect:/admin/user-management";
    }

    @GetMapping("/user-management/create")
    public String newUserPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "admin/user-form";
    }

    @GetMapping("/user-management/update/{id}")
    public String updateUserPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserDTOById(id));
        System.out.println(userService.getUserDTOById(id));
        return "admin/user-form";
    }

    @PostMapping("/user-management/save")
    public String saveUser(@ModelAttribute UserDTO user,
                           @RequestParam(required = false) String dateOfBirthStr,
                           @RequestParam(required = false) String createdAtStr,
                           @RequestParam(required = false) String expirationDateStr,
                           @RequestParam(required = false) MultipartFile imageFile,
                           RedirectAttributes redirectAttributes) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            user.setDateOfBirth(LocalDate.parse(dateOfBirthStr, dateFormatter));
        }

        if (expirationDateStr != null && !expirationDateStr.isEmpty()) {
            user.setExpirationDate(LocalDate.parse(expirationDateStr, dateFormatter));
        }

        if (createdAtStr != null && !createdAtStr.isEmpty()) {
            LocalDate createdDate = LocalDate.parse(createdAtStr, dateFormatter);
            user.setCreatedAt(createdDate.atStartOfDay());
        }

        System.out.println("new user: " + user);
        System.out.println("anh moi " + imageFile.getOriginalFilename());

        try {
            userService.saveUser(user, imageFile);
            String successMessage = (user.getId() == null)
                    ? "ĐÃ THÊM NGƯỜI DÙNG " + user.getStudentId() + " THÀNH CÔNG !"
                    : "CẬP NHẬT NGƯỜI DÙNG " + user.getStudentId() + " THÀNH CÔNG !";

            redirectAttributes.addFlashAttribute("messsage", successMessage);
            return "redirect:/admin/user-management/save-success";
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Username already exists")) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
                return "redirect:/500";
            }
            // Handle other exceptions
            throw e;
        }
    }

    @GetMapping("/user-management/save-success")
    public String saveSuccessPage() {
        return "admin/save-success-page";
    }
}

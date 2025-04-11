package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.SubjectService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // Hiển thị danh sách các chủ đề
    @GetMapping
    public String listSubjects(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        // Trả về view subject-list.html trong thư mục admin
        return "admin/subject-list";
    }

    // Hiển thị form để tạo chủ đề mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        // Trả về view subject-form.html trong thư mục admin
        return "admin/subject-form";
    }

    // Xử lý submit form tạo chủ đề mới (không dùng @Valid)
    @PostMapping
    public String createSubject(@ModelAttribute("subject") Subject subject) {
        subjectService.saveSubject(subject);
        return "redirect:/admin/subject";
    }

    // Hiển thị form để sửa chủ đề đã tồn tại
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Subject> subjectOpt = subjectService.getSubjectById(id);
        if (subjectOpt.isPresent()) {
            model.addAttribute("subject", subjectOpt.get());
            return "admin/subject-form";
        } else {
            // Nếu không tìm thấy, chuyển hướng về trang danh sách
            return "redirect:/admin/subject";
        }
    }

    // Xử lý submit form cập nhật chủ đề (không dùng @Valid)
    @PostMapping("/edit/{id}")
    public String updateSubject(@PathVariable("id") Long id,
                                @ModelAttribute("subject") Subject subject) {
        // Gán lại id cho đối tượng subject sau khi sửa
        subject.setId(id);
        subjectService.updateSubject(subject);
        return "redirect:/admin/subject";
    }

    // Xóa chủ đề theo id (sử dụng POST)
    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubjectById(id);
        return "redirect:/admin/subject";
    }
}

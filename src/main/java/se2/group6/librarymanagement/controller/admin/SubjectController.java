package se2.group6.librarymanagement.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.SubjectService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String listSubjects(@RequestParam(name = "searchSubject", required = false) String searchSubject, Model model) {
        List<Subject> subjects;
        if (searchSubject != null && !searchSubject.isEmpty()) {
            subjects = subjectService.searchByName(searchSubject);
        } else {
            subjects = subjectService.getAllSubjects();
        }
        model.addAttribute("subjects", subjects);
        model.addAttribute("searchSubject", searchSubject);
        return "admin/subject-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "admin/subject-form";
    }

    @PostMapping
    public String createSubject(@ModelAttribute Subject subject,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            subjectService.saveSubject(subject, imageFile);
            redirectAttributes.addFlashAttribute("success", "Lưu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/subject";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Subject> subjectOpt = subjectService.getSubjectById(id);
        if (subjectOpt.isPresent()) {
            model.addAttribute("subject", subjectOpt.get());
            return "admin/subject-form";
        } else {
            return "redirect:/admin/subject";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSubject(@PathVariable("id") Long id, @ModelAttribute("subject") Subject subject) {
        subject.setId(id);
        subjectService.updateSubject(subject);
        return "redirect:/admin/subject";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubjectById(id);
        return "redirect:/admin/subject";
    }
}

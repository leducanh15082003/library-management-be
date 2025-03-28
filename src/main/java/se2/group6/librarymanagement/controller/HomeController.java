package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.SubjectService;
import se2.group6.librarymanagement.utils.SubjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectMapper subjectMapper;

    @GetMapping("/")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(value = "sortType", required = false, defaultValue = "id") String sortType,
            Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        List<SubjectWithBooksDTO> subjectDTOs = subjectMapper.mapSubjectsWithBooks(subjects, sortType);
        model.addAttribute("subjects", subjectDTOs);
        model.addAttribute("sortType", sortType);
        return "home";
    }
}

package se2.group6.librarymanagement.service;

import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    List<Subject> getAllSubjects();
    Optional<Subject> getSubjectById(Long id);
    void saveSubject(Subject subject, MultipartFile file);
    void updateSubject(Subject subject);
    void deleteSubjectById(Long id);
    Subject getSubjectByName(String name);
    List<Subject> searchByName(String name);
}

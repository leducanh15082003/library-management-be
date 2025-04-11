package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    List<Subject> getAllSubjects();
    Optional<Subject> getSubjectById(Long id);
    Subject saveSubject(Subject subject);
    Subject updateSubject(Subject subject);
    void deleteSubjectById(Long id);
}

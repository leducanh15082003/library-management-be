package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}

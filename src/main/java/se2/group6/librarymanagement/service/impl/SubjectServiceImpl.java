package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.repository.SubjectRepository;
import se2.group6.librarymanagement.service.CloudinaryService;
import se2.group6.librarymanagement.service.SubjectService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, CloudinaryService cloudinaryService) {
        this.subjectRepository = subjectRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    @Override
    public void saveSubject(Subject subject, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                subject.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể upload ảnh: " + e.getMessage());
        }

        subjectRepository.save(subject);
    }

    @Override
    public void updateSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public Subject getSubjectByName(String name) {
        return subjectRepository.findByName(name);
    }

    @Override
    public List<Subject> searchByName(String name) {
        return subjectRepository.findByNameContainingIgnoreCase(name);
    }
}

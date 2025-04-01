package se2.group6.librarymanagement.service;

import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void saveUser(User user);
    void updateUser(User user, MultipartFile file);
    void deleteUserById(Long id);
    User getCurrentUser();
    boolean isUserExists(String username);
}

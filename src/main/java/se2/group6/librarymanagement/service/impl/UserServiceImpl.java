package se2.group6.librarymanagement.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.repository.UserRepository;
import se2.group6.librarymanagement.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Cloudinary cloudinary) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, MultipartFile profileImage) {
        User currentUser = getCurrentUser();
        currentUser.setFullName(user.getFullName());
        currentUser.setStudentId(user.getStudentId());
        currentUser.setGender(user.getGender());
        currentUser.setDateOfBirth(user.getDateOfBirth());
        currentUser.setEmail(user.getEmail());
        currentUser.setHometown(user.getHometown());
        currentUser.setFaculty(user.getFaculty());
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");

                currentUser.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload ảnh!", e);
            }
        }
        userRepository.save(currentUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            return userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User Not Found"));
        }
        throw new RuntimeException("User Not Found");
    }

    @Override
    public boolean isUserExists(String username) {
        return userRepository.existsUserByUserName(username);
    }
}

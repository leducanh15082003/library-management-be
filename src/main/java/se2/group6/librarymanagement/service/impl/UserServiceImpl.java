package se2.group6.librarymanagement.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.dto.UserDTO;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.model.enums.Role;
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
    public List<UserDTO> getAllUserDTOs() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.LIBRARY_PATRON)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDTO getUserDTOById(Long id) {
        return mapToDTO(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
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
        currentUser.setProfileCompleted(true);
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

    @Override
    public void saveUser(UserDTO userDTO, MultipartFile imageFile) {
        User user;

        if (userDTO.getId() == null) {
            if (isUserExists(userDTO.getUserName())) {
                throw new RuntimeException("Username already exists");
            }
            
            user = new User();
            user.setPassword("123456"); // Đặt password mặc định khi tạo mới
            user.setRole(Role.LIBRARY_PATRON); // Gán role mặc định
        } else {
            user = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
                    
            if (!user.getUserName().equals(userDTO.getUserName()) && isUserExists(userDTO.getUserName())) {
                throw new RuntimeException("Username already exists");
            }
        }

        user.setFullName(userDTO.getFullName());
        user.setStudentId(userDTO.getStudentId());
        user.setGender(userDTO.getGender());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setEmail(userDTO.getEmail());
        user.setHometown(userDTO.getHometown());
        user.setFaculty(userDTO.getFaculty());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setExpirationDate(userDTO.getExpirationDate());
        user.setUserName(userDTO.getUserName());

        // Xử lý ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                user.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload ảnh!", e);
            }
        }

        userRepository.save(user);
    }


    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name()); // enum → String
        dto.setImageUrl(user.getImageUrl());
        dto.setStudentId(user.getStudentId());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setHometown(user.getHometown());
        dto.setFaculty(user.getFaculty());
        dto.setExpirationDate(user.getExpirationDate());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
    @Override
    public long countAllUsers() {
        return userRepository.count();
    }


}

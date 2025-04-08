package se2.group6.librarymanagement.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

   @Autowired
   private Cloudinary cloudinary;

   public String uploadImage(MultipartFile file) throws IOException {
      Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
      return uploadResult.get("secure_url").toString();
   }
}
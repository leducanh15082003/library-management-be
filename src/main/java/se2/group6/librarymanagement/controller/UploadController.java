package se2.group6.librarymanagement.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.service.BookService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UploadController {

    private final BookService bookService;

    private final Cloudinary cloudinary;

    @Autowired
    public UploadController(Cloudinary cloudinary, BookService bookService) {
        this.cloudinary = cloudinary;
        this.bookService = bookService;
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile imageFile,
                                         @RequestParam("bookId") Long bookId) {
        try {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            // Update Book entity: tìm book theo bookId và set imageUrl
            Optional<Book> bookOptional = bookService.getBookById(bookId);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setImageUrl(imageUrl);
                bookService.saveBook(book);
            }

            return ResponseEntity.ok().body(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}

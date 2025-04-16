package se2.group6.librarymanagement.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.Author;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/document")
public class DocumentManagementController {

   @Autowired
   private BookService bookService;

   @Autowired
   private BookCopyService bookCopyService;

   @Autowired
   private AuthorService authorService;

   @Autowired
   private SubjectService subjectService;

   @Autowired
   private CloudinaryService cloudinaryService;

   @GetMapping("/manage")
   public String manageDocument(
         @RequestParam(value = "title", required = false) String title,
         @RequestParam(value = "category", required = false) String category,
         Model model) {
      List<Book> books;

      if (title != null && !title.trim().isEmpty()) {
         books = bookService.searchBooksByTitle(title);
      } else {
         books = bookService.getAllBooks();
      }

      if (category != null && !category.trim().isEmpty()) {
         try {
            Long categoryId = Long.parseLong(category);
            books = books.stream()
                  .filter(book -> book.getSubject().getId().equals(categoryId))
                  .collect(Collectors.toList());
         } catch (NumberFormatException ignored) {
         }
      }

      List<Subject> subjects = subjectService.getAllSubjects();

      model.addAttribute("books", books);
      model.addAttribute("subjects", subjects);
      model.addAttribute("searchQuery", title);
      model.addAttribute("category", category);

      return "admin/manage-document";
   }

   @GetMapping("/add")
   public String showAddForm(Model model) {
      List<Subject> subjects = subjectService.getAllSubjects();
      List<Author> authors = authorService.getAllAuthors();
      model.addAttribute("subjects", subjects);
      model.addAttribute("authors", authors);
      return "admin/add-document";
   }

   @PostMapping("/add")
   public String addDocument(
         @RequestParam String title,
         @RequestParam String isbn,
         @RequestParam String authorName,
         @RequestParam("subject.id") Long subjectId,
         @RequestParam String publisher,
         @RequestParam String genre,
         @RequestParam(required = false) MultipartFile image) {

      try {
         Book book = new Book();
         book.setTitle(title);
         book.setIsbn(isbn);
         book.setPublisher(publisher);
         book.setGenre(genre);

         // Handle author
         Author author = authorService.findByName(authorName)
               .orElseGet(() -> {
                  Author newAuthor = new Author();
                  newAuthor.setName(authorName);
                  return authorService.saveAuthor(newAuthor);
               });
         book.setAuthor(author);

         // Handle subject
         Subject subject = subjectService.getSubjectById(subjectId)
               .orElseThrow(() -> new RuntimeException("Subject not found"));
         book.setSubject(subject);

         // Handle image upload if provided
         if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            book.setImageUrl(imageUrl);
         }

         bookService.saveBook(book);
         return "redirect:/admin/document/book-copy/add/" + book.getId();
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error adding document: " + e.getMessage());
      }
   }

   @GetMapping("/book-copy/add/{bookId}")
   public String showAddBookCopyForm(@PathVariable Long bookId, Model model) {
      Book book = bookService.getBookById(bookId)
              .orElseThrow(() -> new RuntimeException("Book not found"));
      model.addAttribute("book", book);
      return "admin/book-copy/add";
   }

   @PostMapping("/book-copy/add")
   public String addBookCopies(@RequestParam Long bookId,
                               @RequestParam int numberOfCopies,
                               @RequestParam Long subjectId) {
      Book book = bookService.getBookById(bookId)
              .orElseThrow(() -> new RuntimeException("Book not found"));

      Subject subject = subjectService.getSubjectById(subjectId)
              .orElseThrow(() -> new RuntimeException("Subject not found"));

      long existingCount = bookCopyService.countByBookId(bookId);

      for (int i = 1; i <= numberOfCopies; i++) {
         // Tính số thứ tự của bản sao mới
         long copyNumber = existingCount + i;
         // Tạo barcode theo định dạng "0 + subject_id + 0 + book_id + copy_number"
         String barcode = "0" + subject.getId()
                 + "0" + book.getId()
                 + String.format("%03d", copyNumber);

         BookCopy bookCopy = new BookCopy();
         bookCopy.setBook(book);
         bookCopy.setStatus("AVAILABLE");
         bookCopy.setBarcode(barcode);

         bookCopyService.save(bookCopy);
      }

      return "redirect:/admin/document/manage";
   }

   @GetMapping("/edit/{id}")
   public String showEditForm(@PathVariable Long id, Model model) {
      Book book = bookService.getBookById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
      List<Subject> subjects = subjectService.getAllSubjects();
      List<Author> authors = authorService.getAllAuthors();

      model.addAttribute("book", book);
      model.addAttribute("subjects", subjects);
      model.addAttribute("authors", authors);
      return "admin/edit-document";
   }

   @PostMapping("/edit/{id}")
   public String updateDocument(
         @PathVariable Long id,
         @RequestParam String title,
         @RequestParam String isbn,
         @RequestParam String authorName,
         @RequestParam("subject.id") Long subjectId,
         @RequestParam String publisher,
         @RequestParam String genre,
         @RequestParam(required = false) MultipartFile image) {

      try {
         Book book = bookService.getBookById(id)
               .orElseThrow(() -> new RuntimeException("Book not found"));

         book.setTitle(title);
         book.setIsbn(isbn);
         book.setPublisher(publisher);
         book.setGenre(genre);

         Author author = authorService.findByName(authorName)
               .orElseGet(() -> {
                  Author newAuthor = new Author();
                  newAuthor.setName(authorName);
                  return authorService.saveAuthor(newAuthor);
               });
         book.setAuthor(author);

         Subject subject = subjectService.getSubjectById(subjectId)
               .orElseThrow(() -> new RuntimeException("Subject not found"));
         book.setSubject(subject);

         if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            book.setImageUrl(imageUrl);
         }

         bookService.saveBook(book);
         return "redirect:/admin/document/manage";
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error updating document: " + e.getMessage());
      }
   }

   @PostMapping("/delete/{id}")
   public String deleteDocument(@PathVariable Long id) {
      bookService.deleteBookById(id);
      return "redirect:/admin/document/manage";
   }
}

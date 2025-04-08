package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.Author;
import se2.group6.librarymanagement.repository.AuthorRepository;
import se2.group6.librarymanagement.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
   private final AuthorRepository authorRepository;

   @Autowired
   public AuthorServiceImpl(AuthorRepository authorRepository) {
      this.authorRepository = authorRepository;
   }

   @Override
   public List<Author> getAllAuthors() {
      return authorRepository.findAll();
   }

   @Override
   public Optional<Author> getAuthorById(Long id) {
      return authorRepository.findById(id);
   }

   @Override
   public Optional<Author> findByName(String name) {
      return authorRepository.findByName(name);
   }

   @Override
   public Author saveAuthor(Author author) {
      return authorRepository.save(author);
   }

   @Override
   public Author updateAuthor(Author author) {
      return authorRepository.save(author);
   }

   @Override
   public void deleteAuthorById(Long id) {
      authorRepository.deleteById(id);
   }
}

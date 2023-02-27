package ua.defatov.bookstrackerrest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.defatov.bookstrackerrest.dto.BookRequest;
import ua.defatov.bookstrackerrest.dto.BookResponse;
import ua.defatov.bookstrackerrest.factory.BookFactory;
import ua.defatov.bookstrackerrest.model.Book;
import ua.defatov.bookstrackerrest.repository.BookRepository;
import ua.defatov.bookstrackerrest.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{user_id}/books")
@AllArgsConstructor
public class BookController {

    private static final String BOOK_ID = "{book_id}";

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final BookFactory bookFactory;


    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @PathVariable long user_id
    ) {

        List<Book> books = getBooksIfPresent(user_id);

        if(!books.isEmpty()){

            List<BookResponse> mappedToResponse = books.stream()
                    .map(bookFactory::makeBookResponse)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(mappedToResponse, HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PutMapping
    public ResponseEntity<BookResponse> createBook(
            @PathVariable long user_id,
            BookRequest bookRequest
    ) {

        Book book = bookFactory.makeBookEntity(bookRequest);

        try{
            book.setUser(userRepository.findById(user_id)
                    .orElseThrow(() -> new EntityExistsException("User with id " + user_id + " not found")));
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Book savedUser = bookRepository.save(book);
        BookResponse bookResponse = bookFactory.makeBookResponse(savedUser);

        return new ResponseEntity<>(bookResponse, HttpStatus.OK);

    }

    @GetMapping(path = BOOK_ID)
    public ResponseEntity<BookResponse> readBook(
            @PathVariable long user_id,
            @PathVariable long book_id
    ) {

        List<Book> books = getBooksIfPresent(user_id);

        if(!books.isEmpty()) {

            Book book = books.stream()
                    .filter(b -> b.getId() == book_id)
                    .findFirst()
                    .orElse(null);

            if(book != null) {
                BookResponse bookResponse = bookFactory.makeBookResponse(book);
                return new ResponseEntity<>(bookResponse, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = BOOK_ID)
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable long user_id,
            @PathVariable long book_id,
            BookRequest bookRequest
    ) {

        List<Book> books = getBooksIfPresent(user_id);

        if(!books.isEmpty()){
            Book presentBook = books.stream()
                    .filter(book -> book.getId() == book_id)
                    .findFirst().orElse(null);

            if(presentBook != null) {
                Book updatedBook = bookFactory.makeBookEntity(bookRequest);
                updatedBook.setId(book_id);
                Book savedBook = bookRepository.save(updatedBook);
                BookResponse bookResponse = bookFactory.makeBookResponse(savedBook);

                return new ResponseEntity<>(bookResponse, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(path = BOOK_ID)
    public void deleteBook(
            @PathVariable long user_id,
            @PathVariable long book_id
    ) {
        bookRepository.deleteById(book_id);
    }

    private List<Book> getBooksIfPresent(long user_id) {
        boolean isBooksPresent = userRepository.findById(user_id).isPresent();

        if(isBooksPresent) {
            return userRepository.findById(user_id).get().getBooks();
        }
        return new ArrayList<>();
    }

}

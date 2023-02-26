package ua.defatov.bookstrackerrest.factory;

import org.springframework.stereotype.Component;
import ua.defatov.bookstrackerrest.dto.BookRequest;
import ua.defatov.bookstrackerrest.dto.BookResponse;
import ua.defatov.bookstrackerrest.model.Book;
import ua.defatov.bookstrackerrest.model.Genre;

@Component
public class BookFactory {

    public BookResponse makeBookResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre().name())
                .grade(book.getGrade())
                .description(book.getDescription())
                .build();

    }

    public Book makeBookEntity(BookRequest bookRequest) {

        return Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .genre(Genre.valueOf(bookRequest.getGenre().toUpperCase()))
                .grade(bookRequest.getGrade())
                .description(bookRequest.getDescription())
                .build();

    }

}

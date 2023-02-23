package ua.defatov.bookstrackerrest.dto;

import lombok.Builder;
import lombok.Data;
import ua.defatov.bookstrackerrest.model.Book;

import java.util.List;

@Data
@Builder
public class UserResponse {

    private String firstName;

    private String lastName;

    private String email;

    private List<Book> books;

}

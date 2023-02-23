package ua.defatov.bookstrackerrest.factory;

import org.springframework.stereotype.Component;
import ua.defatov.bookstrackerrest.dto.UserResponse;
import ua.defatov.bookstrackerrest.model.Book;
import ua.defatov.bookstrackerrest.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFactory {

    public UserResponse makeUserResponse(User user) {

        boolean isBooksNoPresent = user.getBooks().isEmpty();

        UserResponse response = UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();

        if(isBooksNoPresent) {
            List<Book> books = new ArrayList<>();
            response.setBooks(books);
            return response;
        }

        response.setBooks(user.getBooks());

        return response;

    }

}

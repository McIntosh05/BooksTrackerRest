package ua.defatov.bookstrackerrest.factory;

import org.springframework.stereotype.Component;
import ua.defatov.bookstrackerrest.dto.UserRequest;
import ua.defatov.bookstrackerrest.dto.UserResponse;
import ua.defatov.bookstrackerrest.model.Book;
import ua.defatov.bookstrackerrest.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFactory {

    public UserResponse makeUserResponse(User user) {

        List<Book> isBooksNoPresent = user.getBooks();

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();

        if(isBooksNoPresent == null) {
            List<Book> books = new ArrayList<>();
            response.setBooks(books);
            return response;
        }

        response.setBooks(user.getBooks());

        return response;

    }

    public User makeUserEntity(UserRequest userRequest) {

        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

    }

}

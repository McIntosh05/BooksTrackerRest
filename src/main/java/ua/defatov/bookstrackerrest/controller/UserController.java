package ua.defatov.bookstrackerrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.defatov.bookstrackerrest.dto.UserRequest;
import ua.defatov.bookstrackerrest.dto.UserResponse;
import ua.defatov.bookstrackerrest.factory.UserFactory;
import ua.defatov.bookstrackerrest.model.User;
import ua.defatov.bookstrackerrest.repository.BookRepository;
import ua.defatov.bookstrackerrest.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    private final UserFactory userFactory;

    public UserController(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {

        List<UserResponse> users = userRepository.findAll().stream()
                .map(userFactory::makeUserResponse)
                .collect(Collectors.toList());

        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);

    }

    @GetMapping(path = "{user_id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long user_id) {

        User user = userRepository.findById(user_id).orElse(null);

        if(user != null) {
            return new ResponseEntity<>(userFactory.makeUserResponse(user), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping(path = "/add")
    public ResponseEntity<UserResponse> registerUser(UserRequest userRequest) {
        User presentUser = userRepository.findAll().stream()
                .filter(usr -> usr.getEmail().equals(userRequest.getEmail()))
                .findFirst().orElse(null);

        if(presentUser == null) {
            User newUser = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .password(userRequest.getPassword())
                    .build();
            UserResponse userResponse = userFactory.makeUserResponse(userRepository.save(newUser));
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}

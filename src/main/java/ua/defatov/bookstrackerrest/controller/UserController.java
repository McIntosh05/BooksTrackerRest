package ua.defatov.bookstrackerrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.defatov.bookstrackerrest.dto.UserRequest;
import ua.defatov.bookstrackerrest.dto.UserResponse;
import ua.defatov.bookstrackerrest.factory.UserFactory;
import ua.defatov.bookstrackerrest.model.User;
import ua.defatov.bookstrackerrest.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@Slf4j
public class UserController {

    private static final String USER_ID = "{user_id}";

    private final UserRepository userRepository;

    private final UserFactory userFactory;

    public UserController(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userRepository.findAll().stream()
                .map(userFactory::makeUserResponse)
                .collect(Collectors.toList());

        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);

    }

    @GetMapping(path = USER_ID)
    public ResponseEntity<UserResponse> readUser(@PathVariable long user_id) {

        User user = userRepository.findById(user_id).orElse(null);

        if(user != null) {
            return new ResponseEntity<>(userFactory.makeUserResponse(user), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        User presentUser = userRepository.findAll().stream()
                .filter(usr -> usr.getEmail().equals(userRequest.getEmail()))
                .findFirst().orElse(null);

        if(presentUser == null) {
            User updatedUser = userFactory.makeUserEntity(userRequest);
            User user = userRepository.save(updatedUser);
            UserResponse userResponse = userFactory.makeUserResponse(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(path = "{user_id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable long user_id,
            UserRequest userRequest
    ) {
        User presentUser = userRepository.findById(user_id).orElse(null);

        if(presentUser != null) {
            User updatedUser = userFactory.makeUserEntity(userRequest);
            updatedUser.setId(user_id);
            User user = userRepository.save(updatedUser);
            UserResponse userResponse = userFactory.makeUserResponse(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping(path = USER_ID)
    public void deleteUser(@PathVariable long user_id) {
        userRepository.deleteById(user_id);
    }


}

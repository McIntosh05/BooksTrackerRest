package ua.defatov.bookstrackerrest.exceptions;

public class InvalidValueException extends RuntimeException{

    public InvalidValueException(String message) {
        super(message);
    }

}

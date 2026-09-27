package fr.miage.debut;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiErrors {
    @ExceptionHandler(InvalidName.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidName(InvalidName error) { return error.getMessage(); }
    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String missing(UserNotFound error) { return error.getMessage(); }
}

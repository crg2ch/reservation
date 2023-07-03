package com.minsoo.reservation.common.handler;

import com.minsoo.reservation.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = PasswordNotMatchException.class)
    public ResponseEntity<?> passwordNotMatch(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExistsEmailException.class)
    public ResponseEntity<?> alreadyExists(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class,
            PartnerNotFoundException.class,
            ShopNotFoundException.class,
            ReservationNotFoundException.class,
            ReviewNotFoundException.class})
    public ResponseEntity<?> notFound(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


}

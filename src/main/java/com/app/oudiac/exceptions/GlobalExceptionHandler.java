package com.app.oudiac.exceptions;

import com.razorpay.RazorpayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // 1. MUST take MethodArgumentNotValidException, not RuntimeException!
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleBlankFieldsExceptions(MethodArgumentNotValidException ex) {
        // For validation errors, getting the message is a bit different to make it clean
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    // 2. Change parameter to UserAlreadyExitException
    @ExceptionHandler(UserAlreadyExitException.class)
    public ResponseEntity<String> handleUserExitExceptions(UserAlreadyExitException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    // 3. Change parameter to UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundExceptions(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    // 4. Change parameter to ItemAlreadyExitException
    @ExceptionHandler(ItemAlreadyExitException.class)
    public ResponseEntity<String> handleItemAlreadyExitException(ItemAlreadyExitException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // 5. Change parameter to ItemAlreadyExitException
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<String> handleAddressNotFoundException(AddressNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RazorpayException.class)
    public ResponseEntity<String> handleRazorpayException(RazorpayException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }


    // 5. Smart Generic Handler to unwrap nested messages
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleError(RuntimeException ex) {
        // If the exception wraps another exception, dig down and get the real message
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

        // As a final cleanup, if the message STILL contains the class name, strip it out
        if (message != null && message.contains(":")) {
            message = message.substring(message.indexOf(":") + 1).trim();
        }

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}

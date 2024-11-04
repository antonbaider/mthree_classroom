package com.mthree.bankmthree.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(status.value(), "Validation Failed", "Invalid input parameters");

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: " + ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatusCode.valueOf(409));
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatusCode.valueOf(409));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<CustomErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        return buildErrorResponse("Error: " + ex.getMessage(), HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(UnauthorizedTransferException.class)
    public ResponseEntity<CustomErrorResponse> handleUnauthorizedTransferException(UnauthorizedTransferException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatusCode.valueOf(403));
    }

    @ExceptionHandler(AccountsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountsNotFoundException(AccountsNotFoundException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("error", "Account Not Found");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SsnAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleSsnAlreadyExistsException(SsnAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatusCode.valueOf(409));
    }

    @ExceptionHandler(IllegalArgumentsException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatusCode.valueOf(400));
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(String message, HttpStatusCode status) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(status.value(), status.toString(), message);
        return new ResponseEntity<>(errorResponse, HttpHeaders.EMPTY, status);
    }
}
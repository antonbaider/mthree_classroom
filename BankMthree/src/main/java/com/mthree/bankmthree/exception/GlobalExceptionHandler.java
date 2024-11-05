package com.mthree.bankmthree.exception;

import com.mthree.bankmthree.exception.account.AccountBalanceNotZeroException;
import com.mthree.bankmthree.exception.account.AccountsNotFoundException;
import com.mthree.bankmthree.exception.account.ReceiverAccountNotFoundException;
import com.mthree.bankmthree.exception.transaction.UnauthorizedTransferException;
import com.mthree.bankmthree.exception.user.UserAlreadyExistsException;
import com.mthree.bankmthree.exception.user.UserNotFoundException;
import com.mthree.bankmthree.exception.user.UserPhoneAlreadyExistsException;
import com.mthree.bankmthree.exception.user.UserSsnAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(String message, String errorCode, HttpStatus status, WebRequest request, Map<String, String> details) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(status.value(), status.getReasonPhrase(), message, errorCode, LocalDateTime.now(), request.getDescription(false).replace("uri=", ""));
        errorResponse.setDetails(details);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> details = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacement) -> existing)); // In case of duplicate keys, keep first

        String message = "Validation failed for one or more fields.";
        return buildErrorResponse(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST, request, details);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        Map<String, String> details = Map.of("cause", "The specified username or email already exists in the system.");
        return buildErrorResponse(ex.getMessage(), "USER_EXISTS", HttpStatus.CONFLICT, request, details);
    }

    @ExceptionHandler(AccountBalanceNotZeroException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountBalanceNotZero(AccountBalanceNotZeroException ex, WebRequest request) {
        Map<String, String> details = Map.of("resolution", "Ensure the account balance is zero before closing.");
        return buildErrorResponse(ex.getMessage(), "ACCOUNT_BALANCE_NON_ZERO", HttpStatus.BAD_REQUEST, request, details);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, String> details = Map.of("resolution", "Check that provided arguments are valid and meet constraints.");
        return buildErrorResponse(ex.getMessage(), "INVALID_ARGUMENT", HttpStatus.BAD_REQUEST, request, details);
    }

    @ExceptionHandler(UserPhoneAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handlePhoneAlreadyExists(UserPhoneAlreadyExistsException ex, WebRequest request) {
        Map<String, String> details = Map.of("cause", "The provided phone number is already associated with another account.");
        return buildErrorResponse(ex.getMessage(), "PHONE_EXISTS", HttpStatus.CONFLICT, request, details);
    }

    @ExceptionHandler(UserSsnAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleSsnAlreadyExists(UserSsnAlreadyExistsException ex, WebRequest request) {
        Map<String, String> details = Map.of("cause", "The provided SSN is already associated with another account.");
        return buildErrorResponse(ex.getMessage(), "SSN_EXISTS", HttpStatus.CONFLICT, request, details);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        Map<String, String> details = Map.of("resolution", "Verify the user ID or username exists in the system.");
        return buildErrorResponse(ex.getMessage(), "USER_NOT_FOUND", HttpStatus.NOT_FOUND, request, details);
    }

    @ExceptionHandler(AccountsNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountsNotFoundException(AccountsNotFoundException ex, WebRequest request) {
        Map<String, String> details = Map.of("resolution", "Ensure the account ID is valid and associated with the correct user.");
        return buildErrorResponse(ex.getMessage(), "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND, request, details);
    }

    @ExceptionHandler(UnauthorizedTransferException.class)
    public ResponseEntity<CustomErrorResponse> handleUnauthorizedTransferException(UnauthorizedTransferException ex, WebRequest request) {
        Map<String, String> details = Map.of("cause", "The user is not authorized to perform this transfer.");
        return buildErrorResponse(ex.getMessage(), "UNAUTHORIZED_TRANSFER", HttpStatus.FORBIDDEN, request, details);
    }

    @ExceptionHandler(ReceiverAccountNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleReceiverAccountNotFoundException(UnauthorizedTransferException ex, WebRequest request) {
        Map<String, String> details = Map.of("cause", "Ensure the receiver account ID is valid and associated with the correct user.");
        return buildErrorResponse(ex.getMessage(), "RECEIVER_ACCOUNT_NOT_FOUND", HttpStatus.FORBIDDEN, request, details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Invalid value provided in the request body.";
        Map<String, String> details = new HashMap<>();

        if (ex.getLocalizedMessage().contains("Cannot deserialize value of type")) {
            String[] parts = ex.getLocalizedMessage().split(": ");
            if (parts.length > 1) {
                message = parts[1].split(";")[0];
                details.put("resolution", "Provide a valid value for the enum field, such as EUR, GBP, USD, PLN, INR.");
            }
        }

        return buildErrorResponse(message, "INVALID_ENUM_VALUE", HttpStatus.BAD_REQUEST, request, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleAllOtherExceptions(Exception ex, WebRequest request) {
        Map<String, String> details = Map.of("cause", "An unexpected error occurred.");
        return buildErrorResponse(ex.getMessage(), "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, request, details);
    }
}
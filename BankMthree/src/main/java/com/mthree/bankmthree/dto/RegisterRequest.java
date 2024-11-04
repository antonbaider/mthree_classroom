package com.mthree.bankmthree.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class RegisterRequest {


    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Schema(description = "Username for login", example = "johndoe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password for login", example = "P@ssw0rd", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    @Schema(description = "Phone number of the user", example = "1234567890")
    private String phone;

    @NotBlank(message = "SSN is required")
    @Pattern(
            regexp = "^(?!000|666|9\\d\\d)(\\d{3})(?!00)(\\d{2})(?!0000)(\\d{4})$",
            message = "Invalid SSN format"
    )
    @Schema(description = "Social Security Number", example = "123456789", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String ssn;
}
package com.mthree.bankmthree.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CloseAccountRequest {
    @NotBlank(message = "Card number is required")
    private String cardNumber;
}
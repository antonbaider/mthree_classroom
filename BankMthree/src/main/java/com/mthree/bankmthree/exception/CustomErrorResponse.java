package com.mthree.bankmthree.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
@Data
public class CustomErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> details;

    public CustomErrorResponse(int status, String error, String message, LocalDateTime timestamp, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }
}
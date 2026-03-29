package com.daw.tfg.exceptions;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String debugMessage;

    public ApiError(int status, String message, String debugMessage) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.debugMessage = debugMessage;
    }
}

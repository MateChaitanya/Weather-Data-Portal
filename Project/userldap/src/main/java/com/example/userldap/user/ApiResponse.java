package com.example.userldap.user;

// This DTO provides a consistent structure for API responses
public class ApiResponse {
    private String message; // A descriptive message (e.g., "Login successful!")
    private Object data;    // The actual data payload (e.g., UserDTO, or null)

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    // Getters (Setters are not strictly needed if only used for responses)
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}

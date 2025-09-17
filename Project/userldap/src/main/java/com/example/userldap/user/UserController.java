package com.example.userldap.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//Allow web pages from diff origin (like react)
@CrossOrigin(origins = "https://localhost")
@RestController

//Base path for all endpoints in this controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Authenticate a user based on email and password
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody UserDTO userDTO) {

        // Log the authentication attempt
        System.out.println("Attempting authentication for user: " + userDTO.getEmailid());

        // Input Validation Block
        // Check if emailid or password are null or empty
        if (userDTO.getEmailid() == null || userDTO.getEmailid().trim().isEmpty() ||
                userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            System.out.println("Authentication failed: Missing email or password.");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Email and Password are required.", null));
        }

        // Authentication Logic Block
        try {
            // Attempt to authenticate the user via UserService
            // Assumes userService.authenticate returns null for invalid credentials
            UserDTO user = userService.authenticate(userDTO.getEmailid(), userDTO.getPassword());
            if (user != null) {
                // If authentication is successful
                System.out.println("User " + userDTO.getEmailid() + " authenticated successfully.");
                user.setPassword(null); // IMPORTANT: remove password from userDTO before sending response
                // Return 200 OK status with a success message and user data
                return ResponseEntity.ok(new ApiResponse("Login successful!", user));
            } else {
                // If authentication returns null (invalid credentials)
                System.out.println("Authentication failed for user: " + userDTO.getEmailid() + " - Invalid credentials.");
                // Return 401 Unauthorized status with an error message
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse("Invalid email or password.", null));
            }
        } catch (Exception e) {
            // Error Handling Block
            // Catch any unexpected exceptions during authentication
            System.err.println("Error during authentication for user " + userDTO.getEmailid() + ": " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            // Return 500 Internal Server Error status with a generic error message
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected server error occurred during login. Please try again later.", null));
        }
    }


    // Register a new user
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody UserDTO userDTO) {

        // Log the registration attempt
        System.out.println("Attempting registration for: FullName=" + userDTO.getFullName() + ", Email=" + userDTO.getEmailid());

        // Input Validation Block
        // Check if required fields in UserDTO are null or empty
        if (userDTO.getFullName() == null || userDTO.getFullName().trim().isEmpty() ||
                userDTO.getEmailid() == null || userDTO.getEmailid().trim().isEmpty() ||
                userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            System.out.println("Registration failed: Missing required fields.");
            // Return a 400 Bad Request status with an error message
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("All fields (Full Name, Email, Password) are required.", null));
        }

        // User Addition Logic Block
        try {
            // Attempt to add the user via UserService
            // Assumes userService.addUser returns the saved UserDTO
            UserDTO savedUser = userService.addUser(userDTO);
            savedUser.setPassword(null); // IMPORTANT: Nullify password before sending response
            System.out.println("User " + userDTO.getEmailid() + " registered successfully.");
            // Return 201 Created status with a success message and saved user data
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Registration successful!", savedUser));
        } catch (RuntimeException ex) {
            // Catch RuntimeException, typically thrown when a user already exists (business logic error)
            System.err.println("User registration conflict for " + userDTO.getEmailid() + ": " + ex.getMessage());
            // Return 409 Conflict status with the specific error message from the exception
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(ex.getMessage(), null));
        } catch (Exception ex) {
            // --- General Error Handling Block ---
            // Catch any other unexpected exceptions during registration
            System.err.println("Error during user registration for " + userDTO.getEmailid() + ": " + ex.getMessage());
            ex.printStackTrace(); // Print stack trace for debugging
            // Return 500 Internal Server Error status with a generic error message
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected server error occurred during registration. Please try again later.", null));
        }
    }

    @GetMapping("/history/{city}/{startDate}/{endDate}")
    public ResponseEntity<List<HistoricalData>> getHistoryByCityName(
            @PathVariable String city,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Assumes userService.getHistoricalDataByCityName returns a List of HistoricalData
        List<HistoricalData> history = userService.getHistoricalDataByCityName(city, startDate, endDate);

        return ResponseEntity.ok(history);
    }
}

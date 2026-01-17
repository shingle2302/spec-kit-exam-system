package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.dto.RegisterRequestDTO;
import com.spec.kit.exam.system.service.AuthService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;

    /**
     * POST /auth/register endpoint for user registration
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            // Call the registration service method
            var user = userService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                registerRequest.getPhone()
            );
            
            return Result.success(user, "User registered successfully");
        } catch (Exception e) {
            return Result.error("4001", e.getMessage());
        }
    }

    /**
     * POST /auth/login endpoint supporting identifier (username/email/phone) and password
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            var authResult = authService.authenticate(loginRequest);
            
            if (authResult.isSuccess()) {
                Map<String, Object> data = new HashMap<>();
                data.put("accessToken", authResult.getToken());
                data.put("user", authResult.getUser());
                
                return Result.success(data, "Login successful");
            } else {
                String message = authResult.getMessage();
                int errorCode = 2001; // Unauthorized error code
                
                // Specific error code for locked accounts
                if (message.contains("locked")) {
                    errorCode = 423; // Locked status code
                }
                
                return Result.error(errorCode, message);
            }
        } catch (Exception e) {
            return Result.error("500", "Authentication failed: " + e.getMessage());
        }
    }

    /**
     * POST /auth/logout endpoint to invalidate sessions
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        // Extract user ID from token (simplified)
        // In a real implementation, you'd validate the token and extract user info
        try {
            // For now, we'll just return a success response
            return Result.success(null, "Logged out successfully");
        } catch (Exception e) {
            return Result.error("500", "Logout failed: " + e.getMessage());
        }
    }
}
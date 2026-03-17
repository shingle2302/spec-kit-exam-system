package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.dto.RegisterRequestDTO;
import com.spec.kit.exam.system.dto.RefreshTokenRequest;
import com.spec.kit.exam.system.dto.TokenResponse;
import com.spec.kit.exam.system.service.AuthService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.JwtUtilEnhanced;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.enums.UserErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private JwtUtilEnhanced jwtUtil;

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
            return Result.error(UserErrorCodeEnum.REGISTRATION_ERROR, e.getMessage());
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
                // Make sure the user object is properly populated with all fields
                data.put("user", authResult.getUser());
                
                return Result.success(data, "Login successful");
            } else {
                String message = authResult.getMessage();
                
                // Return appropriate error codes based on the failure reason
                if (message.toLowerCase().contains("locked")) {
                    // Account locked - use specific error code for locked accounts
                    return Result.error(UserErrorCodeEnum.ACCOUNT_LOCKED, message);
                } else {
                    // General authentication failure
                    return Result.unauthorized(message);
                }
            }
        } catch (Exception e) {
            return Result.error(UserErrorCodeEnum.AUTHENTICATION_FAILED, "Authentication failed: " + e.getMessage());
        }
    }

    /**
     * POST /auth/logout endpoint to invalidate sessions
     */
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from token and invalidate the session
            // In a real implementation, you'd validate the token and extract user info
            authService.logout(token);
            
            return Result.success(null, "Logged out successfully");
        } catch (Exception e) {
            return Result.error(UserErrorCodeEnum.LOGOUT_FAILED, "Logout failed: " + e.getMessage());
        }
    }
    
    /**
     * POST /auth/refresh endpoint to refresh access token
     */
    @PostMapping("/refresh")
    public Result<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            // Validate refresh token
            var claims = jwtUtil.parseToken(request.getRefreshToken());
            
            // Check token type
            if (!"refresh".equals(claims.get("type"))) {
                return Result.error(UserErrorCodeEnum.AUTHENTICATION_FAILED, "无效的刷新令牌");
            }
            
            // Get user info
            Long userId = Long.parseLong(claims.get("userId").toString());
            User user = userService.getById(userId.toString());
            
            // Generate new access token
            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);
            
            TokenResponse response = new TokenResponse();
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken);
            response.setExpiresIn(3600L); // 1 hour in seconds
            
            return Result.success(response, "令牌刷新成功");
            
        } catch (Exception e) {
            return Result.error(UserErrorCodeEnum.AUTHENTICATION_FAILED, "令牌刷新失败: " + e.getMessage());
        }
    }
}
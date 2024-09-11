package com.transfer.controller;

import com.transfer.dto.LoginRequestDTO;
import com.transfer.dto.LoginResponseDTO;
import com.transfer.dto.RegisterCustomerRequest;
import com.transfer.dto.RegisterCustomerResponse;
import com.transfer.exception.custom.CustomerAlreadyExistException;
import com.transfer.exception.response.ErrorDetails;
import com.transfer.service.security.IAuthService;
import com.transfer.service.security.TokenBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Auth Controller", description = "Customer Auth controller")
@CrossOrigin
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new Customer")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RegisterCustomerResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    public RegisterCustomerResponse register(@RequestBody @Valid RegisterCustomerRequest customer) throws CustomerAlreadyExistException {
        return this.authService.register(customer);
    }

    @Operation(summary = "Login and generate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return this.authService.login(loginRequestDTO);
    }

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Operation(summary = "Logout customer", description = "Logout the customer by invalidating their JWT token.")
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String jwt = parseJwtFromRequest(request);
        if (jwt != null) {
            tokenBlacklist.addToken(jwt);  // Add the token to the blacklist
        }
        SecurityContextHolder.clearContext();  // Clear the authentication context
        return ResponseEntity.ok("You have been logged out successfully.");
    }

    private String parseJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

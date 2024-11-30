package com.transfer.controller;

import com.transfer.constants.ApplicationConstants;
import com.transfer.constants.BusinessConstants;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TokenBlacklist tokenBlacklist;

    @PostMapping("/register")
    @Operation(summary = "Register new Customer")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(schema = @Schema(implementation = RegisterCustomerResponse.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_400, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    public RegisterCustomerResponse register(@RequestBody @Valid RegisterCustomerRequest customer) throws CustomerAlreadyExistException {
        return this.authService.register(customer);
    }

    @Operation(summary = "Login and generate JWT")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(schema = @Schema(implementation = LoginResponseDTO.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_401, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return this.authService.login(loginRequestDTO);
    }

    @Operation(summary = "Logout customer", description = "Logout the customer by invalidating their JWT token.")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_400, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_400, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String jwt = authService.parseJwtFromRequest(request);
        if (jwt != null) {
            tokenBlacklist.addToken(jwt);
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApplicationConstants.LOGIN_SUCCESSFULLY.toString());
    }

}

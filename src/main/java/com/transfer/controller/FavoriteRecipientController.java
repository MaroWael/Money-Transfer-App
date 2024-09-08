package com.transfer.controller;

import com.transfer.dto.FavoriteRecipientDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.FavoriteRecipientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteRecipientController {

    private final FavoriteRecipientService favoriteRecipientService;

    @Operation(summary = "Add a Favorite Recipient")
    @ApiResponse(responseCode = "200", description = "Favorite recipient added successfully")
    @PostMapping
    public FavoriteRecipientDTO addFavoriteRecipient(Authentication authentication, @Valid @RequestBody FavoriteRecipientDTO favoriteRecipientDTO) throws ResourceNotFoundException {
        String username = authentication.getName();
        return favoriteRecipientService.addFavoriteRecipient(username, favoriteRecipientDTO);
    }

    @Operation(summary = "Get all Favorite Recipients")
    @ApiResponse(responseCode = "200", description = "List of favorite recipients")
    @GetMapping
    public List<FavoriteRecipientDTO> getFavoriteRecipients(Authentication authentication) throws ResourceNotFoundException {
        String username = authentication.getName();
        return favoriteRecipientService.getFavoriteRecipients(username);
    }

    @DeleteMapping("/account-number/{recipientAccountNumber}")
    public ResponseEntity<String> deleteFavoriteRecipientByAccountNumber(@PathVariable String recipientAccountNumber) throws ResourceNotFoundException {
        favoriteRecipientService.deleteFavoriteRecipientByAccountNumber(recipientAccountNumber);
        return ResponseEntity.ok("Favorite recipient deleted successfully");
    }
}

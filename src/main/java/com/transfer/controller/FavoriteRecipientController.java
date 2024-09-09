package com.transfer.controller;

import com.transfer.dto.DeleteFavoriteRecipientRequestDTO;
import com.transfer.dto.FavoriteRecipientDTO;
import com.transfer.dto.MessageResponseDTO;
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
    @ApiResponse(responseCode = "400", description = "Duplicate favorite recipient")
    @PostMapping("/add")
    public ResponseEntity<MessageResponseDTO> addFavoriteRecipient(Authentication authentication,
                                                                   @Valid @RequestBody FavoriteRecipientDTO favoriteRecipientDTO) {
        try {
            String username = authentication.getName();
            favoriteRecipientService.addFavoriteRecipient(username, favoriteRecipientDTO);
            return ResponseEntity.ok(new MessageResponseDTO("Favorite recipient added successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get all Favorite Recipients")
    @ApiResponse(responseCode = "200", description = "List of favorite recipients")
    @GetMapping
    public List<FavoriteRecipientDTO> getFavoriteRecipients(Authentication authentication) throws ResourceNotFoundException {
        String username = authentication.getName();
        return favoriteRecipientService.getFavoriteRecipients(username);
    }

    @Operation(summary = "Delete a Favorite Recipient")
    @ApiResponse(responseCode = "200", description = "Favorite recipient deleted successfully")
    @ApiResponse(responseCode = "400", description = "Favorite recipient not found")
    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponseDTO> deleteFavoriteRecipient(@RequestBody DeleteFavoriteRecipientRequestDTO request) {
        try {
            favoriteRecipientService.deleteFavoriteRecipientByAccountNumber(request.getRecipientAccountNumber());
            return ResponseEntity.ok(new MessageResponseDTO("Favorite recipient deleted successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
    }
}

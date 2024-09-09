package com.Transfer.controller;

import com.Transfer.DTO.LoginRequestDTO;
import com.Transfer.DTO.LoginResponseDTO;
import com.Transfer.DTO.UpdatePasswordDTO;
import com.Transfer.DTO.RegisterCustomerRequest;
import com.Transfer.DTO.RegisterCustomerResponse;
import com.Transfer.exception.custom.CustomerAlreadyExistException;
import com.Transfer.exception.custom.ResourceNotFoundException;
import com.Transfer.exception.response.ErrorDetails;
import com.Transfer.service.security.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.Transfer.service.security.JwtUtils;
import org.springframework.web.server.ResponseStatusException;

import static com.Transfer.service.security.JwtUtils.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Auth Controller", description = "Customer Auth controller")
public class AuthController {

    private final IAuthService authService;
    private final JwtUtils jwtUtils;

    /**
     *
     * @param customer
     * @return RegisterCustomerResponse
     * @throws CustomerAlreadyExistException
     */

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

    @Operation(summary = "Update Customer Password")
    @ApiResponse(responseCode = "200", description = "Password updated successfully",
            content = {@Content(schema = @Schema(type = "string"), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extract token from header
            // Extract email from token
            String email = jwtUtils.getEmailFromJwtToken(token);

            try {
                authService.updatePassword(email, updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword());
                return ResponseEntity.ok("Password updated successfully");
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is missing or invalid");
        }
    }

}
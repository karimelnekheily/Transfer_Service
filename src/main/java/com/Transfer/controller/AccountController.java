package com.Transfer.controller;
import com.Transfer.DTO.AccountDTO;
import com.Transfer.DTO.CreateAccountDTO;
import com.Transfer.entity.Account;
import com.Transfer.exception.custom.ResourceNotFoundException;
import com.Transfer.exception.response.ErrorDetails;
import com.Transfer.service.IAccountService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Account Controller", description = "Account controller")
public class AccountController {

    private final IAccountService accountService;

    /**
     *
     * @param accountDTO
     * @return AccountDTO
     * @throws ResourceNotFoundException
     */
    @Operation(summary = "Create new Account")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Account.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping
    public AccountDTO createAccount(@Valid @RequestBody CreateAccountDTO accountDTO) throws ResourceNotFoundException {
        return this.accountService.createAccount(accountDTO);
    }
  /*  @PostMapping("/createAccount")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid CreateAccountDTO account) {

        AccountDTO createdAccount = accountService.createAccount(account);

        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);

    }*/

    /**
     *
     * @param accountId
     * @return
     * @throws ResourceNotFoundException
     */
    @Operation(summary = "Get Account by Id")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/{accountId}")
    public Optional<Account> getAccountById(@PathVariable Long accountId) throws ResourceNotFoundException {
        return this.accountService.getAccountById(accountId);
    }
}
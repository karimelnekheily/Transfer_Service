package com.Transfer.service;

import com.Transfer.DTO.AccountDTO;
import com.Transfer.DTO.CreateAccountDTO;
import com.Transfer.DTO.UpdateAccountDTO;
import com.Transfer.entity.Account;
import com.Transfer.exception.custom.ResourceNotFoundException;

import java.util.Optional;

public interface IAccountService {

    /**
     * Create a new account
     *
     * @param accountDTO the account to be created
     * @return the created account
     * @throws ResourceNotFoundException if the account is not found
     */
    AccountDTO createAccount(CreateAccountDTO accountDTO) throws ResourceNotFoundException;

    /**
     * Get account by id
     *
     * @param accountId the account id
     * @return the account
     * @throws ResourceNotFoundException if the account is not found
     */
    Optional<Account> getAccountById(Long accountId) throws ResourceNotFoundException;


    AccountDTO updateAccount(Long accountId, UpdateAccountDTO accountDTO);

    void deleteAccount(Long accountId);

    void deposit(Long accountId, Double amount);


}
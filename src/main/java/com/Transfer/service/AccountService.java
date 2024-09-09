package com.Transfer.service;


import com.Transfer.DTO.AccountDTO;
import com.Transfer.DTO.CreateAccountDTO;
import com.Transfer.DTO.UpdateAccountDTO;
import com.Transfer.entity.Account;
import com.Transfer.entity.Customer;
import com.Transfer.exception.custom.ResourceNotFoundException;
import com.Transfer.repository.AccountRepository;
import com.Transfer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;



    @Override
    @Transactional
    public AccountDTO createAccount(CreateAccountDTO accountDTO) throws ResourceNotFoundException {

        Customer customer = this.customerRepository.findById(accountDTO.getCustomerId()).orElseThrow(()
                -> new ResourceNotFoundException("Customer with id " + accountDTO.getCustomerId() + " not found"));

        Account account = Account.builder()
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .accountType(accountDTO.getAccountType())
                .accountName(accountDTO.getAccountName())
                .accountDescription(accountDTO.getAccountDescription())
                .currency(accountDTO.getCurrency())
                .balance(0.0)
                .customer(customer)
                .build();

        Account savedAccount = this.accountRepository.save(account);

        return savedAccount.toDTO();
    }


    @Override
    public AccountDTO updateAccount(Long accountId, UpdateAccountDTO accountDTO) {
        return null;
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public void deleteAccount(Long accountId) {

    }

    @Override
    public void deposit(Long accountId, Double amount) {

    }
}
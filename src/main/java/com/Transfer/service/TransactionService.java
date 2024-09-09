package com.Transfer.service;

import com.Transfer.DTO.AccountDTO;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Transfer.DTO.TransactionDTO;
import com.Transfer.entity.Account;
import com.Transfer.entity.Transaction;
import com.Transfer.repository.AccountRepository;
import com.Transfer.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private  AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;




    @SneakyThrows
    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) {

//        return transactionRepository.save(transaction);

        Optional<Account> senderAccountOpt = accountService.getAccountById(transactionDTO.getSenderAccountId());
        Optional<Account> recipientAccountOpt = accountService.getAccountById(transactionDTO.getRecipientAccountId());

        if (senderAccountOpt.isPresent() && recipientAccountOpt.isPresent()) {

            Account senderAccount = senderAccountOpt.get();
            Account recipientAccount = recipientAccountOpt.get();

            if (senderAccount.getBalance() < transactionDTO.getAmount()) {
                throw new IllegalArgumentException("Insufficient balance in sender's account");
            }

            senderAccount.setBalance(senderAccount.getBalance() - transactionDTO.getAmount());
            recipientAccount.setBalance(recipientAccount.getBalance() + transactionDTO.getAmount());

            accountRepository.save(senderAccount);
            accountRepository.save(recipientAccount);



            Transaction transaction = Transaction.builder()
                    .senderAccount(senderAccountOpt.get())
                    .recipientAccount(recipientAccountOpt.get())
                    .amount(transactionDTO.getAmount())
                    .currency(transactionDTO.getCurrency())
                    .status(transactionDTO.getStatus())
                    .description(transactionDTO.getDescription())
                    .build();
            return transactionRepository.save(transaction);
        } else {
            throw new IllegalArgumentException("Invalid account IDs");
        }




    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

//    public List<Transaction> getTransactionsBySenderAccountId(Long senderAccountId) {
//        return transactionRepository.findBySenderAccountId(senderAccountId);
//    }
//
//    public List<Transaction> getTransactionsByRecipientAccountId(Long recipientAccountId) {
//        return transactionRepository.findByRecipientAccountId(recipientAccountId);
//    }
//
//    public List<Transaction> getTransactionsByAccountIdAndDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
//        return transactionRepository.findBySenderAccountIdOrRecipientAccountIdAndTransactionDateBetween(
//                accountId, accountId, startDate, endDate
//        );
//    }


    public List<Transaction> getTransactionHistory(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null && endDate == null) {
            return transactionRepository.findBySenderAccountIdOrRecipientAccountId(  accountId,  accountId);

        } else {

            return transactionRepository.findBySenderAccountIdOrRecipientAccountIdAndTransactionDateBetween(
                    accountId, accountId, startDate, endDate);
        }
    }
}
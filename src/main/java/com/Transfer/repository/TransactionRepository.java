package com.Transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Transfer.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccountId(Long senderAccountId);

    List<Transaction> findByRecipientAccountId(Long recipientAccountId);

    List<Transaction> findBySenderAccountIdOrRecipientAccountIdAndTransactionDateBetween(
            Long accountId, Long recipientAccountId, LocalDateTime startDate, LocalDateTime endDate
    );

    List<Transaction> findBySenderAccountIdOrRecipientAccountId(Long accountId, Long accountId1);
}
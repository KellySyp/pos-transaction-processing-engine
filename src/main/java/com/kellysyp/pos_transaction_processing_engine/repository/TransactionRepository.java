package com.kellysyp.pos_transaction_processing_engine.repository;

import com.kellysyp.pos_transaction_processing_engine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByMerchantIdAndLast4AndCreatedAtBetween(
            String merchantId,
            String last4,
            Instant start,
            Instant end
    );
}
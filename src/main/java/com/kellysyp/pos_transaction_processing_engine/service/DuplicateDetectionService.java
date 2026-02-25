package com.kellysyp.pos_transaction_processing_engine.service;

import com.kellysyp.pos_transaction_processing_engine.dto.PaymentRequest;
import com.kellysyp.pos_transaction_processing_engine.model.Transaction;
import com.kellysyp.pos_transaction_processing_engine.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DuplicateDetectionService {

    private static final int DUPLICATE_WINDOW_MINUTES = 5;

    private final TransactionRepository transactionRepository;

    public DuplicateDetectionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean isDuplicate(PaymentRequest request) {

        Instant now = Instant.now();
        Instant fiveMinutesAgo = now.minusSeconds(300);

        List<Transaction> recentTransactions =
                transactionRepository.findByMerchantIdAndLast4AndCreatedAtBetween(
                        request.getMerchantId(),
                        request.getLast4(),
                        fiveMinutesAgo,
                        now
                );

        System.out.println("Now: " + now);
        System.out.println("Five minutes ago: " + fiveMinutesAgo);
        System.out.println("Found transactions: " + recentTransactions.size());

        return recentTransactions.stream()
                .anyMatch(t -> t.getAmount().compareTo(request.getAmount()) == 0);
    }
}
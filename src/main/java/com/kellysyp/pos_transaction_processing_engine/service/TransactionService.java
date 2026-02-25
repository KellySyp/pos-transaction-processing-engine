package com.kellysyp.pos_transaction_processing_engine.service;

import com.kellysyp.pos_transaction_processing_engine.dto.PaymentRequest;
import com.kellysyp.pos_transaction_processing_engine.dto.PaymentResponse;
import com.kellysyp.pos_transaction_processing_engine.model.ResponseCode;
import com.kellysyp.pos_transaction_processing_engine.model.Transaction;
import com.kellysyp.pos_transaction_processing_engine.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionService {

    private final DuplicateDetectionService duplicateDetectionService;
    private final TransactionRepository transactionRepository;

    public TransactionService(DuplicateDetectionService duplicateDetectionService,
                              TransactionRepository transactionRepository) {
        this.duplicateDetectionService = duplicateDetectionService;
        this.transactionRepository = transactionRepository;
    }

    public PaymentResponse processPayment(PaymentRequest request) {

        if (request.getCardNumber() == null || request.getCardNumber().length() != 16) {
            return new PaymentResponse(
                    ResponseCode.INVALID_CARD.getCode(),
                    ResponseCode.INVALID_CARD.getDescription(),
                    null
            );
        }

        if (request.getAmount().doubleValue() > 1000) {
            return new PaymentResponse(
                    ResponseCode.INSUFFICIENT_FUNDS.getCode(),
                    ResponseCode.INSUFFICIENT_FUNDS.getDescription(),
                    null
            );
        }

        if (duplicateDetectionService.isDuplicate(request)) {

            Transaction duplicateTransaction = new Transaction();
            duplicateTransaction.setAmount(request.getAmount());
            duplicateTransaction.setCurrency(request.getCurrency());
            duplicateTransaction.setAmount(request.getAmount());
            duplicateTransaction.setMerchantId(request.getMerchantId());
            duplicateTransaction.setLast4(request.getLast4());
            duplicateTransaction.setStatus("DECLINED");
            duplicateTransaction.setResponseCode("94");
            duplicateTransaction.setCreatedAt(Instant.now());

            transactionRepository.save(duplicateTransaction);

            return new PaymentResponse(
                    "94",
                    "Duplicate transaction detected",
                    null
            );
        }

        // ✅ SAVE APPROVED TRANSACTION
        String transactionId = UUID.randomUUID().toString();

        Transaction approvedTransaction = new Transaction();
        approvedTransaction.setAmount(request.getAmount());
        approvedTransaction.setCurrency(request.getCurrency());
        approvedTransaction.setMerchantId(request.getMerchantId());
        approvedTransaction.setLast4(request.getLast4());
        approvedTransaction.setStatus("APPROVED");
        approvedTransaction.setResponseCode(ResponseCode.APPROVED.getCode());
        approvedTransaction.setCreatedAt(Instant.now());

        transactionRepository.save(approvedTransaction);

        return new PaymentResponse(
                ResponseCode.APPROVED.getCode(),
                ResponseCode.APPROVED.getDescription(),
                transactionId
        );
    }
}
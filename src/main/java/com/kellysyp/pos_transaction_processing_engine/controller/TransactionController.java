package com.kellysyp.pos_transaction_processing_engine.controller;

import com.kellysyp.pos_transaction_processing_engine.dto.PaymentRequest;
import com.kellysyp.pos_transaction_processing_engine.dto.PaymentResponse;
import com.kellysyp.pos_transaction_processing_engine.model.Transaction;
import com.kellysyp.pos_transaction_processing_engine.repository.TransactionRepository;
import com.kellysyp.pos_transaction_processing_engine.service.AuthorizationResult;
import com.kellysyp.pos_transaction_processing_engine.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    public TransactionController(TransactionRepository transactionRepository, AuthorizationService authorizationService) {
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public Map<String, Object> createTransaction(@Valid @RequestBody Map<String, Object> request) {

        Transaction transaction = new Transaction();
        transaction.setTransactionType((String) request.get("transactionType"));
        transaction.setAmount(new BigDecimal(request.get("amount").toString()));
        transaction.setCurrency((String) request.get("currency"));
        transaction.setMerchantId((String) request.get("merchantId"));
        transaction.setTerminalId((String) request.get("terminalId"));
        transaction.setBin((String) request.get("bin"));
        transaction.setLast4((String) request.get("last4"));
        transaction.setStatus("RECEIVED");

        Transaction saved = transactionRepository.save(transaction);

        return Map.of(
                "transactionId", saved.getTransactionId(),
                "status", saved.getStatus()
        );
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {

        AuthorizationResult result = authorizationService.authorize(
                request.getAmount(),
                request.getBin()
        );

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setBin(request.getBin());
        transaction.setStatus(result.getStatus());
        transaction.setCurrency(request.getCurrency());

        Transaction saved = transactionRepository.save(transaction);

        PaymentResponse response = new PaymentResponse(
                saved.getTransactionId(),
                result.getStatus(),
                result.getResponseCode()
        );

        return ResponseEntity.ok(response);
    }

}
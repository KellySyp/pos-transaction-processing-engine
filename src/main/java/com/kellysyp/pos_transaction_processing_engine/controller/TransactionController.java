package com.kellysyp.pos_transaction_processing_engine.controller;

import com.kellysyp.pos_transaction_processing_engine.dto.PaymentRequest;
import com.kellysyp.pos_transaction_processing_engine.dto.PaymentResponse;
import com.kellysyp.pos_transaction_processing_engine.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions") // base path
public class TransactionController {

    private final TransactionService transactionService;

    // Constructor injection
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody PaymentRequest request) {

        // Call the service
        PaymentResponse response = transactionService.processPayment(request);

        return ResponseEntity.ok(response);
    }
}
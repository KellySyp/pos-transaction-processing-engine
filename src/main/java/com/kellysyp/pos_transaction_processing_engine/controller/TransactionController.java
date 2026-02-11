package com.kellysyp.pos_transaction_processing_engine.controller;

import com.kellysyp.pos_transaction_processing_engine.dto.PaymentRequest;
import com.kellysyp.pos_transaction_processing_engine.dto.PaymentResponse;
import com.kellysyp.pos_transaction_processing_engine.model.Transaction;
import com.kellysyp.pos_transaction_processing_engine.repository.TransactionRepository;
import com.kellysyp.pos_transaction_processing_engine.service.AuthorizationResult;
import com.kellysyp.pos_transaction_processing_engine.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {

        PaymentResponse response = new PaymentResponse();

        try {

            // 1️⃣ Validate card
            if (request.getCardNumber() == null || request.getCardNumber().length() < 12) {
                response.setStatus("DECLINED");
                response.setMessage("Invalid Card");
                return ResponseEntity.badRequest().body(response);
            }

            // 2️⃣ Forced test scenarios
            if (request.getTestScenario() != null) {

                switch (request.getTestScenario()) {

                    case "INSUFFICIENT_FUNDS":
                        response.setStatus("DECLINED");
                        response.setMessage("Insufficient Funds");
                        return ResponseEntity.ok(response);

                    case "INVALID_CARD":
                        response.setStatus("DECLINED");
                        response.setMessage("Invalid Card");
                        return ResponseEntity.ok(response);

                    case "TIMEOUT":
                        Thread.sleep(5000); // simulate delay
                        throw new RuntimeException("Gateway Timeout");

                    default:
                        break;
                }
            }

            // 3️⃣ Random decline simulation (10% insufficient funds)
            if (Math.random() < 0.1) {
                response.setStatus("DECLINED");
                response.setMessage("Insufficient Funds");
                return ResponseEntity.ok(response);
            }

            // 4️⃣ Otherwise approve
            response.setStatus("APPROVED");
            response.setMessage("Transaction Approved");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setStatus("ERROR");
            response.setMessage("System Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
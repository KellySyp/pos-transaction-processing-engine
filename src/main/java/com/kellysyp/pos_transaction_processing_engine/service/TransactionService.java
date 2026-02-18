package com.kellysyp.pos_transaction_processing_engine.service;

import com.kellysyp.pos_transaction_processing_engine.dto.PaymentRequest;
import com.kellysyp.pos_transaction_processing_engine.dto.PaymentResponse;
import com.kellysyp.pos_transaction_processing_engine.model.ResponseCode;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

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

        String transactionId = UUID.randomUUID().toString();

        return new PaymentResponse(
                ResponseCode.APPROVED.getCode(),
                ResponseCode.APPROVED.getDescription(),
                transactionId
        );
    }
}

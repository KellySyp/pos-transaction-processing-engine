package com.kellysyp.pos_transaction_processing_engine.dto;

import java.util.UUID;

public class PaymentResponse {

    private String responseCode;
    private String message;
    private String transactionId;

    // ✅ Required for Spring/Jackson
    public PaymentResponse() {
    }

    // ✅ Your custom constructor
    public PaymentResponse(String responseCode, String message, String transactionId) {
        this.responseCode = responseCode;
        this.message = message;
        this.transactionId = transactionId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
package com.kellysyp.pos_transaction_processing_engine.service;

public class AuthorizationResult {

    private final String status;
    private final String responseCode;
    private final String message;

    private AuthorizationResult(String status, String responseCode, String message) {
        this.status = status;
        this.responseCode = responseCode;
        this.message = message;
    }

    public static AuthorizationResult approved() {
        return new AuthorizationResult("AUTHORIZED", "00", "Approved");
    }

    public static AuthorizationResult declined(String code, String message) {
        return new AuthorizationResult("DECLINED", code, message);
    }

    public String getStatus() {
        return status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }
}
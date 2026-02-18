package com.kellysyp.pos_transaction_processing_engine.model;

public enum ResponseCode {

    APPROVED("00", "Approved"),
    INSUFFICIENT_FUNDS("51", "Insufficient Funds"),
    INVALID_CARD("14", "Invalid Card Number"),
    ISSUER_INOPERATIVE("91", "Issuer or Switch Inoperative"),
    DO_NOT_HONOR("05", "Do Not Honor");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }
}
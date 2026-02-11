package com.kellysyp.pos_transaction_processing_engine.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class AuthorizationService {

    @Value("${authorization.max-amount:500}")
    private BigDecimal maxAmount;

    private static final Set<String> BLOCKED_BINS = Set.of(
            "400000",
            "411111"
    );

    public AuthorizationResult authorize(BigDecimal amount, String bin) {

        if (amount.compareTo(maxAmount) > 0) {
            return AuthorizationResult.declined("05", "Amount exceeds limit");
        }

        if (BLOCKED_BINS.contains(bin)) {
            return AuthorizationResult.declined("14", "Invalid card number");
        }

        return AuthorizationResult.approved();
    }
}
package com.kellysyp.pos_transaction_processing_engine.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private UUID transactionId;

    private String transactionType;
    private Double amount;
    private String merchantId;
    private String last4;
    private Instant createdAt = Instant.now();
}
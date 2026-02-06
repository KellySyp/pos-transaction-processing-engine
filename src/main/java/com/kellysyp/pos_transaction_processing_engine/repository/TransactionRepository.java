package com.kellysyp.pos_transaction_processing_engine.repository;

import com.kellysyp.pos_transaction_processing_engine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
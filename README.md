# POS Transaction Processing Engine

A simplified Point-of-Sale (POS) transaction processing engine designed to simulate real-world card payment flows, including transaction intake, authorization, de-duplication, and audit logging.

## Overview
This project represents the merchant-side transaction processing layer in a payment ecosystem. It integrates with a mock Payment Gateway to simulate end-to-end card authorization scenarios.

## Core Features
- POS transaction intake (purchase, refund, void)
- Transaction lifecycle management
- De-duplication detection
- Authorization via Payment Gateway Simulator
- Audit logging and error handling

## Tech Stack
- Java / Spring Boot
- PostgreSQL
- REST APIs

## Payment Flow
1. POS submits transaction
2. Transaction is validated and stored
3. Authorization request sent to Payment Gateway
4. Response processed and transaction state updated

## Future Enhancements
- Settlement and batch processing
- Reconciliation reporting
- Chargeback lifecycle
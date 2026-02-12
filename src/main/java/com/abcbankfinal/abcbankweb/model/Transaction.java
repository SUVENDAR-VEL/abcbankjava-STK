package com.abcbankfinal.abcbankweb.model;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "date_of_transaction", nullable = false)
    private LocalDate dateOfTransaction;

    @Column(name = "closing_balance", nullable = false)
    private Double closingBalance;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;  // DEPOSIT / WITHDRAW

    @Column(name = "transactioned_amount", nullable = false)
    private Double transactionedAmount;

    // ---------------------------
    // Many Transactions → One Account
    // ---------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;
}

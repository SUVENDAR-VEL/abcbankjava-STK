package com.abcbankfinal.abcbankweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "card_number", nullable = false, unique = true)
    private Long cardNumber;

    @Column(name = "current_limit")
    private Double currentLimit;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "status")
    private String status;

    @Column(name = "cvv_number")
    private String cvvNumber;

    @Column(name = "max_limit")
    private Double maxLimit;

    @ManyToOne
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;
}

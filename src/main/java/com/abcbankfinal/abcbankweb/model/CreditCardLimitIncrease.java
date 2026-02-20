package com.abcbankfinal.abcbankweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "credit_limit_increase")
public class CreditCardLimitIncrease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "increase_credit_id")
    private Long increaseCreditLimitId;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(name = "requested_limit", nullable = false)
    private Double requestedLimit;

    @Column(name = "current_limit_at_request", nullable = false)
    private Double currentLimitAtRequest;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "remarks", length = 225)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;
}
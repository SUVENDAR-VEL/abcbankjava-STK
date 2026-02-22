package com.abcbankfinal.abcbankweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "queries")
public class Queries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queries_id")
    private Long queriesId;

    @Column(name = "customer_query", length = 225)
    private String customerQuery;

    @Column(name = "requested_date")
    private LocalDate queryRaisedDate;

    @Column(name = "remarks", length = 225)
    private String queryResponse;

    @Column(name = "approved_by")
    private Integer queryApprovedBy;

    @Column(name = "approved_date")
    private LocalDate queryApprovedDate;

    @Column(name = "status", length = 10)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;
}
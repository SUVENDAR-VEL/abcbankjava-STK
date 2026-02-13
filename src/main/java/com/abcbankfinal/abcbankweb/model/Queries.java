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

    @Column(name = "query_raised_date")
    private LocalDate queryRaisedDate;

    @Column(name = "query_response", length = 225)
    private String queryResponse;

    @Column(name = "query_approved_by")
    private Integer queryApprovedBy;

    @Column(name = "query_approved_date")
    private LocalDate queryApprovedDate;

    @Column(name = "status", length = 10)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;
}
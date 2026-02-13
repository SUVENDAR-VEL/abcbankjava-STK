package com.abcbankfinal.abcbankweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "cheque_request")
public class ChequeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cheque_request_id")
    private Integer chequeRequestId;

    @Column(name = "no_of_leaves", nullable = false)
    private Integer noOfLeaves;

    @Column(name = "requested_date")
    private LocalDate requestedDate;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "remarks", length = 225)
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

}
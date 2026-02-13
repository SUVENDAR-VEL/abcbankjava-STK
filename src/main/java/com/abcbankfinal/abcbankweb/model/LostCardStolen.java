package com.abcbankfinal.abcbankweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "lost_card_stolen")
@Data
public class LostCardStolen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_card_id")
    private Long lostCardId;

    @Column(name = "lost_card_stolen_date")
    private LocalDate lostCardStolenDate;

    @Column(name = "lost_card_number")
    private Long lostCardNumber;

    @Column(name = "created_date")
    private LocalDate createdDate;

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

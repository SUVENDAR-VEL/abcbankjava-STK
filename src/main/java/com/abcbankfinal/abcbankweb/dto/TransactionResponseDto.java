package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionResponseDto {

    private Long transactionId;
    private LocalDate dateOfTransaction;
    private String transactionType;
    private Double transactionedAmount;
    private Double closingBalance;
    private String transactionCode;
}

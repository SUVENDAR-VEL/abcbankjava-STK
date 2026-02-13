package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDto {

    private Long cardId;
    private Long cardNumber;
    private Double currentLimit;
    private LocalDate issuedDate;
    private String expiryDate;
    private String status;
    private Double maxLimit;

    private String cardTypeName;   // From CardType
    private Long accountNumber;    // From Account
}

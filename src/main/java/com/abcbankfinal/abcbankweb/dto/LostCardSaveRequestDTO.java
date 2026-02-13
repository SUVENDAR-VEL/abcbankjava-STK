package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LostCardSaveRequestDTO {

    private LocalDate lostCardStolenDate;
    private Long lostCardNumber;
    private Long accountNumber;
}

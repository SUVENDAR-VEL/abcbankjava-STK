package com.abcbankfinal.abcbankweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LostCardResponseDTO {

    private Long lostCardId;
    private Long lostCardNumber;
    private LocalDate lostCardStolenDate;
    private String status;
    private String remarks;
    private Long accountNumber;
}

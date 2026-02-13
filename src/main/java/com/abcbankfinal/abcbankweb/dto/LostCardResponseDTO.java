package com.abcbankfinal.abcbankweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LostCardResponseDTO {

    private Long lostCardId;              // 1
    private Long lostCardNumber;           // 2
    private LocalDate lostCardStolenDate;  // 3
    private String status;                 // 4
    private String remarks;                // 5
    private Long accountNumber;             // 6
    private LocalDate createdDate;          // 7
    private Long approvedBy;                // 8
    private LocalDate approvedDate;         // 9
}

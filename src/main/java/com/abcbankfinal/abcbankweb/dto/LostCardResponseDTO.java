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
    private LocalDate lostCardStolenDate;
    private String status;
    private String remarks;
    private Long accountNumber;             // 6
    private LocalDate createdDate;
    private Long approvedBy;
    private LocalDate approvedDate;
    private String fullName;


}

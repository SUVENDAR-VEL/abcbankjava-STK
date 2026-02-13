package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ChequeRequestDto {

    private Integer chequeRequestId;
    private Integer noOfLeaves;
    private LocalDate requestedDate;
    private Integer approvedBy;
    private LocalDate approvedDate;
    private String status;
    private String remarks;
    private Long accountNumber;

}
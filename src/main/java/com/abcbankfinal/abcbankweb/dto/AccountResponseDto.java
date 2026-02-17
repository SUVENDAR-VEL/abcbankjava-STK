package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AccountResponseDto {

    private Long accountNumber;
    private Double balance;
    private String branchName;
    private String branchCode;
    private String city;
    private String state;
    private String status;
    private String accountTypeName;
    private LocalDate openedDate;
}
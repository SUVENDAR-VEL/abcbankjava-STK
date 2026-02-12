package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class AccountResponseDto {

    private Long accountNumber;
    private Double balance;
    private String branchName;
    private String branchCode;
    private String city;
    private String state;
    private String status;
}
package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class CreditLimitIncreaseSaveDto {

    private Double requestedLimit;
    private Long accountNumber;
}

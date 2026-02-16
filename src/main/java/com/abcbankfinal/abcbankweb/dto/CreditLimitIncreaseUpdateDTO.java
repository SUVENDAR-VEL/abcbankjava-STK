package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class CreditLimitIncreaseUpdateDTO {
    private String action;
    private String remarks;
    private Long approvedById;
}

package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreditLimitIncreaseResponseDto {

    private Long increaseCreditLimitId;
    private Double requestedLimit;
    private Double currentLimitAtRequest;
    private LocalDate requestDate;

    private Integer approvedBy;
    private LocalDate approvedDate;
    private String remarks;

    private String status;
    private Long cardNumber;
    private Long accountNumber;
    private String fullName;
    private String mobileNumber;
    private String city;
    private String email;
    private String approvedByName;
}
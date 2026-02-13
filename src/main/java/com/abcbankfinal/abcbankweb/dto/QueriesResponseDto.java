package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QueriesResponseDto {

    private Long queriesId;
    private String customerQuery;
    private LocalDate queryRaisedDate;

    private String queryResponse;
    private Integer queryApprovedBy;
    private LocalDate queryApprovedDate;

    private String status;
    private Long accountNumber;
}

package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class RequestCountDto {

    private long total;
    private long approved;
    private long rejected;
    private long pending;
}
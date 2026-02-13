package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class LostCardListRequestDTO {

    private String status;
    private int page = 0;
    private int size = 10;
}
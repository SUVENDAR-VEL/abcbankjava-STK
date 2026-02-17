package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class UserSearchRequest {

    private String status;   // optional

    private int page = 0;
    private int size = 10;
}

package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class UserSearchRequest {
    private String status;
    private Long roleId;
    private int page = 0;
    private int size = 10;
}

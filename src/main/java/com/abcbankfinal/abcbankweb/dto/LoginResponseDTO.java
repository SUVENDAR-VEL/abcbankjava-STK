package com.abcbankfinal.abcbankweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String email;
    private String message;
    private int roleId;
}

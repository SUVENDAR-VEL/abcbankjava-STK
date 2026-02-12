package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserResponseDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String city;
    private String state;
    private String roleName;

    private List<AccountResponseDto> accounts;
}
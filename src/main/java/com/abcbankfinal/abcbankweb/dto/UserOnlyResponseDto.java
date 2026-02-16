package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserOnlyResponseDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String city;
    private String state;
    private String roleName;
}
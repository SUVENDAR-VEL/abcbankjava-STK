package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserRequestDto {

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String pancard;
    private String aadhar;
    private Integer roleId;
    private Integer accountTypeId;
    private double initialBalance;
    private String branchName;
    private String branchCode;
    private String accountCity;
    private String accountState;
}

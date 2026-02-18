package com.abcbankfinal.abcbankweb.dto;

import lombok.Data;

@Data
public class UserContactUpdateDto {

    private String mobileNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
}

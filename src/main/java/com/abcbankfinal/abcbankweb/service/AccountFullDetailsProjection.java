package com.abcbankfinal.abcbankweb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AccountFullDetailsProjection {

    Long getUserId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getMobileNumber();
    String getAlternativeNumber();
    String getAddress();
    String getCity();
    String getState();
    String getCountry();
    String getPincode();
    String getPancard();
    String getAadhar();
    LocalDate getDateOfBirth();
    LocalDateTime getCreatedDate();

    Integer getRoleId();
    String getRoleName();

    Long getAccountNumber();
    Double getBalance();
    String getAccountStatus();
    String getBranchName();
    String getBranchCode();
    LocalDate getOpenedDate();

    // 🔴 VERY IMPORTANT — must match alias EXACTLY
    Integer getAccountTypeId();
    String getAccountTypeName();
}

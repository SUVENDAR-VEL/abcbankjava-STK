package com.abcbankfinal.abcbankweb.service;

public interface UserAccountListProjection {

    Long getUserId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getMobileNumber();
    String getCity();
    String getState();

    Long getAccountNumber();
    String getAccountStatus();
    String getAccountType();

    Long getRoleId();
}

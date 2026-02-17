package com.abcbankfinal.abcbankweb.service;

public interface AccountFullDetailsProjection {

    // 🔹 User Details
    Long getUserId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getMobileNumber();
    String getAlternativeNumber();
    String getCity();
    String getState();
    String getCountry();
    String getAddress();

    // 🔹 Role Details
    Long getRoleId();
    String getRoleName();

    // 🔹 Account Details
    Long getAccountNumber();
    Double getBalance();
    String getAccountStatus();
    String getBranchName();
    String getBranchCode();

    // 🔹 Account Type
    String getAccountType();
}

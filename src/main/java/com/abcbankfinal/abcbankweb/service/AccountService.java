package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.AccountResponseDto;
import com.abcbankfinal.abcbankweb.dto.CardDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface AccountService {

    ApiResponse<List<AccountResponseDto>> getAccountsByUserId(Long userId);

    AccountFullDetailsProjection getAccountFullDetails(Long accountNumber);

    ApiResponse<List<AccountResponseDto>> getActiveAccountsByUserId(Long userId);
}
package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.TransactionResponseDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;


public interface TransactionService {

    ApiResponse<List<TransactionResponseDto>> getTransactionsByAccountNumber(Long accountNumber);
}

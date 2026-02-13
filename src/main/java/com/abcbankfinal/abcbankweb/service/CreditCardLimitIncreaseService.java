package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseResponseDto;
import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseSaveDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface CreditCardLimitIncreaseService {

    ApiResponse<CreditLimitIncreaseResponseDto>
    save(CreditLimitIncreaseSaveDto dto);

    ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccountNumber(Long accountNumber);
}
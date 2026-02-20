package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface CreditCardLimitIncreaseService {

    ApiResponse<CreditLimitIncreaseResponseDto>
    saveCreditLimitIncrease(CreditLimitIncreaseSaveDto dto);

    ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByCardNumber(Long cardNumber);

    ApiResponse<PageResponse<CreditLimitIncreaseResponseDto>>
    getAllCreditLimitIncreases(CreditLimitIncreaseListRequestDTO request);

    ApiResponse<CreditLimitIncreaseResponseDto>
    getCreditLimitIncreaseById(Long id);

    ApiResponse<String>
    updateCreditLimitIncreaseStatus(Long id,
                                    CreditLimitIncreaseUpdateDTO request);

    ApiResponse<RequestCountDto>
    getCreditLimitIncreaseCounts();
}
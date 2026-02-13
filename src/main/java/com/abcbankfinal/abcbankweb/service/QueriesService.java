package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.QueriesResponseDto;
import com.abcbankfinal.abcbankweb.dto.QueriesSaveDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import java.util.List;


public interface QueriesService {

    ApiResponse<QueriesResponseDto> save(QueriesSaveDto dto);
    ApiResponse<List<QueriesResponseDto>> getByAccountNumber(Long accountNumber);
}
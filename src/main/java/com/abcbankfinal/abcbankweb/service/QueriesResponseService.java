package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.QueriesResponseDto;

import java.util.List;

public interface QueriesResponseService {

    List<QueriesResponseDto> getAllQueries();

    QueriesResponseDto getQueryById(Long queryId);

    List<QueriesResponseDto> getQueriesByStatus(String status);

    List<QueriesResponseDto> getQueriesByAccountNumber(Long accountNumber);

    QueriesResponseDto updateQueryStatus(Long id, String status);
}

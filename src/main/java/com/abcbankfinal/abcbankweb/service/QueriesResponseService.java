package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface QueriesResponseService {

    ApiResponse<QueriesResponseDto> saveQuery(
            QueriesSaveDto dto);

    ApiResponse<List<QueriesResponseDto>> getByAccountNumber(
            Long accountNumber);

    ApiResponse<PageResponse<QueriesResponseDto>> getAllQueries(
            QueriesListRequestDto request);

    ApiResponse<QueriesResponseDto> getQueryById(
            Long queryId);

    ApiResponse<String> updateQueryStatus(
            Long queryId,
            QueriesUpdateRequestDto request);

    ApiResponse<RequestCountDto>
    getQueriesCounts();
}
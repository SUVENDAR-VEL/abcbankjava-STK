package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface ChequeRequestService {

    ApiResponse<ChequeRequestDto> saveChequeRequest(
            ChequeRequestDto dto);

    ApiResponse<List<ChequeRequestDto>> getByAccountNumber(
            Long accountNumber);

    ApiResponse<PageResponse<ChequeRequestDto>> getAllChequeRequests(
            ChequeListRequestDTO request);

    ApiResponse<ChequeRequestDto> getChequeById(
            Integer id);

    ApiResponse<String> updateChequeStatus(
            Integer id,
            ChequeUpdateRequestDTO request);

    ApiResponse<RequestCountDto> getChequeRequestCounts();
}
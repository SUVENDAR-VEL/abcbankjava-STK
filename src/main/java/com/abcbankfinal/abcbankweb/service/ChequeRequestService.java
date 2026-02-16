package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.ChequeListRequestDTO;
import com.abcbankfinal.abcbankweb.dto.ChequeRequestDto;
import com.abcbankfinal.abcbankweb.dto.ChequeUpdateRequestDTO;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChequeRequestService {

    ApiResponse<ChequeRequestDto> saveChequeRequest(
            ChequeRequestDto dto);

    ApiResponse<List<ChequeRequestDto>> getByAccountNumber(
            Long accountNumber);

    ApiResponse<Page<ChequeRequestDto>> getAllChequeRequests(
            ChequeListRequestDTO request);

    ApiResponse<ChequeRequestDto> getChequeById(Integer id);

    ApiResponse<String> updateChequeStatus(
            Integer id,
            ChequeUpdateRequestDTO request);
}
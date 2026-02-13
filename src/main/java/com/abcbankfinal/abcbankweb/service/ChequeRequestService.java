package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.ChequeRequestDto;

import java.util.List;

public interface ChequeRequestService {

    ChequeRequestDto saveChequeRequest(ChequeRequestDto dto);

    List<ChequeRequestDto> getByAccountNumber(Long accountNumber);
}
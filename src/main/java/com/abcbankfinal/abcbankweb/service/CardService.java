package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.dto.CardDto;

import java.util.List;

public interface CardService {

    ApiResponse<List<CardDto>> getCardsByAccountNumber(Long accountNumber);
}

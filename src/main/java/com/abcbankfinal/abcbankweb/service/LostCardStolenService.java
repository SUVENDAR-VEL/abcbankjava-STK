package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.LostCardResponseDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardSaveRequestDTO;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface LostCardStolenService {

    ApiResponse<String> saveLostCard(LostCardSaveRequestDTO request);

    ApiResponse<List<LostCardResponseDTO>> getLostCardsByAccountNumber(Long accountNumber);
}

package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.LostCardListRequestDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardResponseDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardSaveRequestDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardUpdateRequestDTO;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LostCardStolenService {

    ApiResponse<String> saveLostCard(LostCardSaveRequestDTO request);

    ApiResponse<List<LostCardResponseDTO>> getLostCardsByAccountNumber(Long accountNumber);

    ApiResponse<Page<LostCardResponseDTO>> getAllLostCards(LostCardListRequestDTO request);

    ApiResponse<LostCardResponseDTO> getById(Long id);

    ApiResponse<String> updateLostCard(Long id, LostCardUpdateRequestDTO request);
}

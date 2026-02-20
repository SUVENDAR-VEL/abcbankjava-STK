package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

import java.util.List;

public interface LostCardStolenService {

    ApiResponse<String> saveLostCard(LostCardSaveRequestDTO request);

    ApiResponse<List<LostCardResponseDTO>>
    getLostCardsByCardNumber(Long cardNumber);

    ApiResponse<LostCardResponseDTO> getById(Long id);

    ApiResponse<String> updateLostCard(
            Long id,
            LostCardUpdateRequestDTO request);

    ApiResponse<PageResponse<LostCardResponseDTO>>
    getAllLostCards(LostCardListRequestDTO request);

    ApiResponse<RequestCountDto> getLostCardCounts();
}
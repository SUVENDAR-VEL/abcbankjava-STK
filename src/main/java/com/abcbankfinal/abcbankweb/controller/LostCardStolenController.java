package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.LostCardListByAccountRequestDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardListRequestDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardResponseDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardSaveRequestDTO;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.LostCardStolenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lostCard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LostCardStolenController {

    private final LostCardStolenService lostCardStolenService;

    @PostMapping("/save")
    public ApiResponse<String> saveLostCard(
            @RequestBody LostCardSaveRequestDTO request) {
        return lostCardStolenService.saveLostCard(request);
    }

    @PostMapping("/lastRequestList")
    public ApiResponse<List<LostCardResponseDTO>> getByAccountNumber(
            @RequestBody LostCardListByAccountRequestDTO request) {

        return lostCardStolenService.getLostCardsByAccountNumber(request.getAccountNumber());
    }


    @PostMapping("/adminLastCardList")
    public ApiResponse<Page<LostCardResponseDTO>> listLostCards(
            @RequestBody LostCardListRequestDTO request) {

        return lostCardStolenService.getAllLostCards(request);
    }


    @GetMapping("/lastCardBy/{id}")
    public ApiResponse<LostCardResponseDTO> getById(@PathVariable Long id) {
        return lostCardStolenService.getById(id);

    }
}
package com.abcbankfinal.abcbankweb.controller;
import com.abcbankfinal.abcbankweb.dto.*;
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

    @PostMapping("/lostRequestList")
    public ApiResponse<List<LostCardResponseDTO>> getByAccountNumber(
            @RequestBody LostCardListByAccountRequestDTO request) {

        return lostCardStolenService.getLostCardsByAccountNumber(request.getAccountNumber());
    }


    @PostMapping("/adminLastCardList")
    public ApiResponse<PageResponse<LostCardResponseDTO>> listLostCards(
            @RequestBody LostCardListRequestDTO request) {

        return lostCardStolenService.getAllLostCards(request);
    }


    @GetMapping("/lostCardBy/{id}")
    public ApiResponse<LostCardResponseDTO> getById(@PathVariable Long id) {
        return lostCardStolenService.getById(id);

    }

    @PostMapping("/lostCardUpdateAdmin/{id}")
    public ApiResponse<String> updateLostCard(
            @PathVariable Long id,
            @RequestBody LostCardUpdateRequestDTO request) {
        return lostCardStolenService.updateLostCard(id, request);
    }

    @GetMapping("/count")
    public ApiResponse<RequestCountDto>
    getLostCardCounts() {

        return lostCardStolenService
                .getLostCardCounts();
    }
}

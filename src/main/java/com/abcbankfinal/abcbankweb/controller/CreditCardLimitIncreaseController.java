package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.CreditCardLimitIncreaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditLimit")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CreditCardLimitIncreaseController {

    private final CreditCardLimitIncreaseService service;

    @PostMapping("/save")
    public ApiResponse<CreditLimitIncreaseResponseDto>
    saveCreditLimitIncrease(
            @RequestBody CreditLimitIncreaseSaveDto dto) {

        return service
                .saveCreditLimitIncrease(dto);
    }

    @PostMapping("/creditLimitListByAccount")
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccountNumber(
            @RequestBody CreditLimitIncreaseListByAccountRequestDTO request) {

        return service
                .getByAccountNumber(
                        request.getAccountNumber());
    }

    @PostMapping("/adminCreditLimitList")
    public ApiResponse<PageResponse<CreditLimitIncreaseResponseDto>>
    getAllCreditLimitIncreases(
            @RequestBody CreditLimitIncreaseListRequestDTO request) {

        return service
                .getAllCreditLimitIncreases(request);
    }

    @GetMapping("/creditLimitBy/{id}")
    public ApiResponse<CreditLimitIncreaseResponseDto>
    getCreditLimitIncreaseById(
            @PathVariable Long id) {

        return service
                .getCreditLimitIncreaseById(id);
    }

    @PostMapping("/creditLimitUpdateAdmin/{id}")
    public ApiResponse<String>
    updateCreditLimitIncreaseStatus(
            @PathVariable Long id,
            @RequestBody CreditLimitIncreaseUpdateDTO request) {

        return service
                .updateCreditLimitIncreaseStatus(id, request);
    }

    @GetMapping("/count")
    public ApiResponse<RequestCountDto>
    getCreditLimitIncreaseCounts() {

        return service
                .getCreditLimitIncreaseCounts();
    }

}
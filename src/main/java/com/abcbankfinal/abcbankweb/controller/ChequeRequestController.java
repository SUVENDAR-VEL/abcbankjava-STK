package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.ChequeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chequeRequest")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChequeRequestController {

    private final ChequeRequestService chequeRequestService;

    @PostMapping("/save")
    public ApiResponse<ChequeRequestDto> saveChequeRequest(
            @RequestBody ChequeRequestDto dto) {

        return chequeRequestService.saveChequeRequest(dto);
    }

    @PostMapping("/chequeRequestList")
    public ApiResponse<List<ChequeRequestDto>>
    getByAccountNumber(
            @RequestBody ChequeListByAccountRequestDTO request) {

        return chequeRequestService
                .getByAccountNumber(request.getAccountNumber());
    }

    @PostMapping("/adminChequeList")
    public ApiResponse<PageResponse<ChequeRequestDto>>
    listChequeRequests(
            @RequestBody ChequeListRequestDTO request) {

        return chequeRequestService
                .getAllChequeRequests(request);
    }

    @GetMapping("/chequeBy/{id}")
    public ApiResponse<ChequeRequestDto>
    getChequeById(@PathVariable Integer id) {

        return chequeRequestService.getChequeById(id);
    }

    @PostMapping("/chequeUpdateAdmin/{id}")
    public ApiResponse<String> updateChequeStatus(
            @PathVariable Integer id,
            @RequestBody ChequeUpdateRequestDTO request) {

        return chequeRequestService
                .updateChequeStatus(id, request);
    }

    @GetMapping("/counts")
    public ApiResponse<RequestCountDto>
    getChequeRequestCounts() {

        return chequeRequestService
                .getChequeRequestCounts();
    }
}
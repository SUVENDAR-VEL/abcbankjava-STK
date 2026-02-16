package com.abcbankfinal.abcbankweb.controller;
import com.abcbankfinal.abcbankweb.dto.ChequeListRequestDTO;
import com.abcbankfinal.abcbankweb.dto.ChequeRequestDto;
import com.abcbankfinal.abcbankweb.dto.ChequeUpdateRequestDTO;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.ChequeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chequeRequest")
@CrossOrigin("*")
public class ChequeRequestController {

    private final ChequeRequestService chequeRequestService;

    public ChequeRequestController(ChequeRequestService chequeRequestService) {
        this.chequeRequestService = chequeRequestService;
    }

    @PostMapping("/save")
    public ApiResponse<ChequeRequestDto> saveChequeRequest(
            @RequestBody ChequeRequestDto dto) {

        return chequeRequestService
                .saveChequeRequest(dto);
    }

    @GetMapping("/{accountNumber}")
    public ApiResponse<List<ChequeRequestDto>>
    getByAccountNumber(
            @PathVariable Long accountNumber) {

        return chequeRequestService
                .getByAccountNumber(accountNumber);
    }

    @PostMapping("/adminChequeList")
    public ApiResponse<Page<ChequeRequestDto>> listChequeRequests(
            @RequestBody ChequeListRequestDTO request) {

        return chequeRequestService
                .getAllChequeRequests(request);
    }

    @GetMapping("/chequeBy/{id}")
    public ApiResponse<ChequeRequestDto> getChequeById(
            @PathVariable Integer id) {

        return chequeRequestService.getChequeById(id);
    }

    @PostMapping("/chequeUpdateAdmin/{id}")
    public ApiResponse<String> updateChequeStatus(
            @PathVariable Integer id,
            @RequestBody ChequeUpdateRequestDTO request) {

        return chequeRequestService
                .updateChequeStatus(id, request);
    }
}
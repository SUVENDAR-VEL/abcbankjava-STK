package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseResponseDto;
import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseSaveDto;
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
    save(@RequestBody CreditLimitIncreaseSaveDto dto) {
        return service.save(dto);
    }

    @GetMapping("/getBy/{accountNumber}")
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccount(@PathVariable Long accountNumber) {
        return service.getByAccountNumber(accountNumber);
    }
}

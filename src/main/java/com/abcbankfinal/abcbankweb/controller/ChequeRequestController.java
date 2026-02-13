package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.ChequeRequestDto;
import com.abcbankfinal.abcbankweb.service.ChequeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chequeRequest")
@CrossOrigin
public class ChequeRequestController {

    @Autowired
    private ChequeRequestService chequeRequestService;

    @PostMapping("/save")
    public ChequeRequestDto saveChequeRequest(@RequestBody ChequeRequestDto dto) {
        return chequeRequestService.saveChequeRequest(dto);
    }

    @GetMapping("/{accountNumber}")
    public List<ChequeRequestDto> getByAccountNumber(@PathVariable Long accountNumber) {
        return chequeRequestService.getByAccountNumber(accountNumber);
    }
}
package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.TransactionResponseDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>>
    getTransactions(@PathVariable Long accountNumber) {

        return ResponseEntity.ok(
                transactionService.getTransactionsByAccountNumber(accountNumber)
        );
    }
}

package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.AccountResponseDto;
import com.abcbankfinal.abcbankweb.dto.CardDto;
import com.abcbankfinal.abcbankweb.dto.TransactionResponseDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.AccountService;
import com.abcbankfinal.abcbankweb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @GetMapping("/userAccounts/{userId}")
    public ResponseEntity<ApiResponse<List<AccountResponseDto>>>
    getAccountsByUserId(@PathVariable Long userId) {

        return ResponseEntity.ok(
                accountService.getAccountsByUserId(userId)
        );
    }

    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>>
    getTransactions(@PathVariable Long accountNumber) {

        return ResponseEntity.ok(
                transactionService.getTransactionsByAccountNumber(accountNumber)
        );
    }

    @GetMapping("/userCardList/{accountNumber}")
    public ResponseEntity<ApiResponse<List<CardDto>>>
    getCardsByAccountNumber(@PathVariable Long accountNumber) {
        return ResponseEntity.ok(
                transactionService.getCardsByAccountNumber(accountNumber)
        );
    }
}

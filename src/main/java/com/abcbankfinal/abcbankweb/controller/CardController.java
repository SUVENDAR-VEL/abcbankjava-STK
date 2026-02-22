package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.CardDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/account/{accountNumber}")
    public ApiResponse<List<CardDto>> getCardsByAccountNumber(@PathVariable Long accountNumber) {
        return cardService.getCardsByAccountNumber(accountNumber);
    }

    @GetMapping("/account/{accountNumber}/credit")
    public ApiResponse<List<CardDto>> getActiveCreditCardsByAccountNumber(
            @PathVariable Long accountNumber) {

        return cardService.getActiveCreditCardsByAccountNumber(accountNumber);
    }
}

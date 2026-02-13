package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.CardDto;
import com.abcbankfinal.abcbankweb.dto.TransactionResponseDto;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.Transaction;
import com.abcbankfinal.abcbankweb.repository.CardRepository;
import com.abcbankfinal.abcbankweb.repository.TransactionRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.TransactionService;
import com.abcbankfinal.abcbankweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public ApiResponse<List<TransactionResponseDto>>
    getTransactionsByAccountNumber(Long accountNumber) {

        List<Transaction> transactions =
                transactionRepository.findByAccountAccountNumberOrderByDateOfTransactionDesc(accountNumber);

        if (transactions.isEmpty()) {
            throw new RuntimeException("No transactions found for this account");
        }

        List<TransactionResponseDto> responseList = transactions.stream().map(tx -> {
            TransactionResponseDto dto = new TransactionResponseDto();
            dto.setTransactionId(tx.getTransactionId());
            dto.setDateOfTransaction(tx.getDateOfTransaction());
            dto.setTransactionType(tx.getTransactionType());
            dto.setTransactionedAmount(tx.getTransactionedAmount());
            dto.setClosingBalance(tx.getClosingBalance());
            dto.setClosingBalance(tx.getClosingBalance());
            return dto;
        }).toList();

        return new ApiResponse<>(
                true,
                "Transaction List Fetched Successfully",
                responseList
        );
    }


    @Override
    public ApiResponse<List<CardDto>> getCardsByAccountNumber(Long accountNumber) {
        List<Card> cards = cardRepository.findByAccountAccountNumber(accountNumber);
        if (cards == null || cards.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "No cards found for this account",
                    null
            );
        }

        List<CardDto> response = cards.stream().map(card -> {
            CardDto dto = new CardDto();
            dto.setCardId(card.getCardId());
            dto.setCardNumber(card.getCardNumber());
            dto.setCurrentLimit(card.getCurrentLimit());
            dto.setIssuedDate(card.getIssuedDate());
            dto.setExpiryDate(card.getExpiryDate());
            dto.setStatus(card.getStatus());
            dto.setMaxLimit(card.getMaxLimit());
            dto.setCardTypeName(card.getCardType().getCardTypeName());
            dto.setAccountNumber(card.getAccount().getAccountNumber());

            return dto;
        }).collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Cards fetched successfully",
                response
        );
    }


}
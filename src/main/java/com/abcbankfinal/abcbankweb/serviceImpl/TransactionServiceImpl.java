package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.CardDto;
import com.abcbankfinal.abcbankweb.dto.TransactionResponseDto;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.Transaction;
import com.abcbankfinal.abcbankweb.repository.CardRepository;
import com.abcbankfinal.abcbankweb.repository.TransactionRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.TransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Override
    public ApiResponse<List<TransactionResponseDto>>
    getTransactionsByAccountNumber(Long accountNumber) {

        List<Transaction> transactions =
                transactionRepository
                        .findByAccountAccountNumberOrderByDateOfTransactionDesc(
                                accountNumber
                        );

        if (transactions.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "No transactions found for this account",
                    null
            );
        }

        List<TransactionResponseDto> responseList =
                transactions.stream().map(tx -> {
                    TransactionResponseDto dto = new TransactionResponseDto();
                    dto.setTransactionId(tx.getTransactionId());
                    dto.setDateOfTransaction(tx.getDateOfTransaction());
                    dto.setTransactionType(tx.getTransactionType());
                    dto.setTransactionedAmount(tx.getTransactionedAmount());
                    dto.setClosingBalance(tx.getClosingBalance());
                    return dto;
                }).collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Transaction List Fetched Successfully",
                responseList
        );
    }

    @Override
    public ApiResponse<List<CardDto>> getCardsByAccountNumber(Long accountNumber) {

        // ✅ UPDATED → Fetch only ACTIVE cards
        List<Card> cards =
                cardRepository
                        .findByAccountAccountNumberAndStatusIgnoreCase(
                                accountNumber,
                                "ACTIVE"
                        );

        if (cards.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "No ACTIVE cards found for this account",
                    null
            );
        }

        List<CardDto> response =
                cards.stream().map(card -> {
                    CardDto dto = new CardDto();
                    dto.setCardId(card.getCardId());
                    dto.setCardNumber(card.getCardNumber());
                    dto.setCurrentLimit(card.getCurrentLimit());
                    dto.setIssuedDate(card.getIssuedDate());
                    dto.setExpiryDate(card.getExpiryDate());
                    dto.setStatus(card.getStatus());
                    dto.setMaxLimit(card.getMaxLimit());
                    dto.setCardTypeName(
                            card.getCardType().getCardTypeName()
                    );
                    dto.setAccountNumber(
                            card.getAccount().getAccountNumber()
                    );
                    return dto;
                }).collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Active Cards Fetched Successfully",
                response
        );
    }
}
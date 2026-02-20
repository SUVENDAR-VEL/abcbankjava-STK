package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.CardDto;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.repository.CardRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public ApiResponse<List<CardDto>> getCardsByAccountNumber(Long accountNumber) {

        // Fetch only ACTIVE cards
        List<Card> cards =
                cardRepository
                        .findByAccountAccountNumberAndStatusIgnoreCase(
                                accountNumber,
                                "ACTIVE"
                        );

        if (cards.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "No active cards found for this account number",
                    null
            );
        }

        List<CardDto> cardDtos =
                cards.stream()
                        .map(card -> {
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
                        })
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Active cards fetched successfully",
                cardDtos
        );
    }
}
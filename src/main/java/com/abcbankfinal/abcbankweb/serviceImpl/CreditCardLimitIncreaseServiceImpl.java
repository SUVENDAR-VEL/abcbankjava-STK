package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseResponseDto;
import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseSaveDto;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.CardRepository;
import com.abcbankfinal.abcbankweb.repository.CreditCardLimitIncreaseRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.CreditCardLimitIncreaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardLimitIncreaseServiceImpl
        implements CreditCardLimitIncreaseService {

    private final CreditCardLimitIncreaseRepository repository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    save(CreditLimitIncreaseSaveDto dto) {

        Account account = accountRepository
                .findById(dto.getAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        boolean hasCard =
                cardRepository.existsByAccountAccountNumber(
                        dto.getAccountNumber()
                );

        if (!hasCard) {
            return new ApiResponse<>(
                    false,
                    "This account does not have any credit card",
                    null
            );
        }

        CreditCardLimitIncrease entity =
                new CreditCardLimitIncrease();

        entity.setRequestedLimit(dto.getRequestedLimit());
        entity.setRequestDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setAccount(account);

        CreditCardLimitIncrease saved =
                repository.save(entity);

        return new ApiResponse<>(
                true,
                "Credit Limit Increase Request Saved",
                mapToDto(saved)
        );
    }

    @Override
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccountNumber(Long accountNumber) {

        List<CreditCardLimitIncrease> list =
                repository.findByAccountAccountNumber(accountNumber);

        if (list.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "No Requests Found",
                    null
            );
        }

        List<CreditLimitIncreaseResponseDto> response =
                list.stream().map(this::mapToDto).toList();

        return new ApiResponse<>(
                true,
                "Requests Fetched Successfully",
                response
        );
    }

    private CreditLimitIncreaseResponseDto mapToDto(
            CreditCardLimitIncrease e) {

        CreditLimitIncreaseResponseDto dto =
                new CreditLimitIncreaseResponseDto();

        dto.setIncreaseCreditLimitId(e.getIncreaseCreditLimitId());
        dto.setRequestedLimit(e.getRequestedLimit());
        dto.setRequestDate(e.getRequestDate());

        dto.setApprovedBy(e.getApprovedBy());
        dto.setApprovedDate(e.getApprovedDate());
        dto.setRemarks(e.getRemarks());

        dto.setStatus(e.getStatus());
        dto.setAccountNumber(
                e.getAccount().getAccountNumber()
        );

        return dto;
    }
}

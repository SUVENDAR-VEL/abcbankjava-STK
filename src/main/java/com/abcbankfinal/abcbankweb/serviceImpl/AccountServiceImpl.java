package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.AccountResponseDto;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public ApiResponse<List<AccountResponseDto>> getAccountsByUserId(Long userId) {

        List<Account> accounts =
                accountRepository.findByCustomerUserId(userId);

        if (accounts.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "No Accounts Found For This User",
                    null
            );
        }

        List<AccountResponseDto> response =
                accounts.stream().map(acc -> {

                    AccountResponseDto dto =
                            new AccountResponseDto();

                    dto.setAccountNumber(acc.getAccountNumber());
                    dto.setBalance(acc.getBalance());
                    dto.setBranchName(acc.getBranchName());
                    dto.setBranchCode(acc.getBranchCode());
                    dto.setCity(acc.getCity());
                    dto.setState(acc.getState());
                    dto.setStatus(acc.getStatus());
                    dto.setOpenedDate(acc.getOpenedDate());

                    if (acc.getAccountType() != null) {
                        dto.setAccountTypeName(
                                acc.getAccountType()
                                        .getAccountTypeName()
                        );
                    }

                    return dto;

                }).collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Accounts Fetched Successfully",
                response
        );
    }
}
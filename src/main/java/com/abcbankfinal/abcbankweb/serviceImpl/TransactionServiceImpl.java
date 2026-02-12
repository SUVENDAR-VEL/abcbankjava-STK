package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.TransactionResponseDto;
import com.abcbankfinal.abcbankweb.model.Transaction;
import com.abcbankfinal.abcbankweb.repository.TransactionRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.TransactionService;
import com.abcbankfinal.abcbankweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;

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
            return dto;
        }).toList();

        return new ApiResponse<>(
                true,
                "Transaction List Fetched Successfully",
                responseList
        );
    }


}
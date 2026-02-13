package com.abcbankfinal.abcbankweb.serviceImpl;



import com.abcbankfinal.abcbankweb.dto.QueriesResponseDto;
import com.abcbankfinal.abcbankweb.dto.QueriesSaveDto;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.Queries;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.QueriesRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.QueriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueriesServiceImpl implements QueriesService {

    private final QueriesRepository queriesRepository;
    private final AccountRepository accountRepository;

    @Override
    public ApiResponse<QueriesResponseDto> save(QueriesSaveDto dto) {

        Account account = accountRepository
                .findById(dto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account Not Found"));

        Queries query = new Queries();
        query.setCustomerQuery(dto.getCustomerQuery());
        query.setQueryRaisedDate(LocalDate.now());
        query.setStatus("Pending");
        query.setAccount(account);

        Queries saved = queriesRepository.save(query);

        QueriesResponseDto res = mapToDto(saved);

        return new ApiResponse<>(true, "Query Saved Successfully", res);
    }

    @Override
    public ApiResponse<List<QueriesResponseDto>> getByAccountNumber(Long accountNumber) {

        List<Queries> list =
                queriesRepository.findByAccountAccountNumber(accountNumber);

        if (list.isEmpty()) {
            return new ApiResponse<>(false, "No Queries Found", null);
        }

        List<QueriesResponseDto> response =
                list.stream().map(this::mapToDto).toList();

        return new ApiResponse<>(true, "Queries Fetched Successfully", response);
    }

    private QueriesResponseDto mapToDto(Queries q) {

        QueriesResponseDto dto = new QueriesResponseDto();

        dto.setQueriesId(q.getQueriesId());
        dto.setCustomerQuery(q.getCustomerQuery());
        dto.setQueryRaisedDate(q.getQueryRaisedDate());

        dto.setQueryResponse(q.getQueryResponse());
        dto.setQueryApprovedBy(q.getQueryApprovedBy());
        dto.setQueryApprovedDate(q.getQueryApprovedDate());

        dto.setStatus(q.getStatus());
        dto.setAccountNumber(
                q.getAccount().getAccountNumber()
        );
        return dto;
    }
}
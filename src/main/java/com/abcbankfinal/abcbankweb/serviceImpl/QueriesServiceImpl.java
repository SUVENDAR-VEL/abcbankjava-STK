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

        Account account = accountRepository.findById(dto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account Not Found"));

        Queries query = new Queries();
        query.setCustomerQuery(dto.getCustomerQuery());
        query.setQueryRaisedDate(LocalDate.now());
        query.setStatus("Pending");
        query.setAccount(account);

        Queries saved = queriesRepository.save(query);

        return new ApiResponse<>(true, "Query Saved Successfully", mapToDto(saved));
    }

    @Override
    public ApiResponse<List<QueriesResponseDto>> getByAccountNumber(Long accountNumber) {

        List<Queries> list = queriesRepository.findByAccountAccountNumber(accountNumber);

        if (list.isEmpty()) {
            return new ApiResponse<>(false, "No Queries Found", null);
        }

        List<QueriesResponseDto> response =
                list.stream().map(this::mapToDto).toList();

        return new ApiResponse<>(true, "Queries Fetched Successfully", response);
    }

    @Override
    public ApiResponse<QueriesResponseDto> updateQueryStatus(Long id, String status) {

        Queries queries = queriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Query Not Found"));

        queries.setStatus(status);
        queries.setQueryApprovedDate(LocalDate.now());
        queries.setQueryApprovedBy(1); // temp admin id

        Queries updated = queriesRepository.save(queries);

        return new ApiResponse<>(true, "Query Updated Successfully", mapToDto(updated));
    }

    @Override
    public ApiResponse<List<QueriesResponseDto>> getAllQueries() {

        List<Queries> list = queriesRepository.findAll();

        if (list.isEmpty()) {
            return new ApiResponse<>(false, "No Queries Found", null);
        }

        List<QueriesResponseDto> response =
                list.stream().map(this::mapToDto).toList();

        return new ApiResponse<>(true, "Queries Fetched Successfully", response);
    }

    @Override
    public ApiResponse<QueriesResponseDto> getQueryById(Long id) {

        Queries queries = queriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Query Not Found"));

        return new ApiResponse<>(true, "Query fetched successfully", mapToDto(queries));
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

        if (q.getAccount() != null) {
            dto.setAccountNumber(q.getAccount().getAccountNumber());
        }
        return dto;
    }
}

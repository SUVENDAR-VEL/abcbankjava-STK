package com.abcbankfinal.abcbankweb.serviceImpl;
import com.abcbankfinal.abcbankweb.dto.QueriesResponseDto;
import com.abcbankfinal.abcbankweb.model.Queries;
import com.abcbankfinal.abcbankweb.repository.QueriesResponseRepository;
import com.abcbankfinal.abcbankweb.service.QueriesResponseService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueriesResponseServiceImpl implements QueriesResponseService {

    private final QueriesResponseRepository queriesResponseRepository;

    public QueriesResponseServiceImpl(QueriesResponseRepository queriesResponseRepository) {
        this.queriesResponseRepository = queriesResponseRepository;
    }

    @Override
    public List<QueriesResponseDto> getAllQueries() {
        return queriesResponseRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QueriesResponseDto getQueryById(Long queryId) {
        Queries queries = queriesResponseRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with id: " + queryId));

        return mapToDto(queries);
    }

    @Override
    public List<QueriesResponseDto> getQueriesByStatus(String status) {
        return queriesResponseRepository.findByStatus(status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QueriesResponseDto> getQueriesByAccountNumber(Long accountNumber) {
        return queriesResponseRepository.findByAccount_AccountNumber(accountNumber)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QueriesResponseDto updateQueryStatus(Long id, String status) {
        return null;
    }

    private QueriesResponseDto mapToDto(Queries queries) {
        QueriesResponseDto dto = new QueriesResponseDto();

        dto.setQueriesId(queries.getQueriesId());
        dto.setCustomerQuery(queries.getCustomerQuery());
        dto.setQueryRaisedDate(queries.getQueryRaisedDate());
        dto.setQueryResponse(queries.getQueryResponse());
        dto.setQueryApprovedBy(queries.getQueryApprovedBy());
        dto.setQueryApprovedDate(queries.getQueryApprovedDate());
        dto.setStatus(queries.getStatus());

        if (queries.getAccount() != null) {
            dto.setAccountNumber(queries.getAccount().getAccountNumber());
        }

        return dto;
    }
}

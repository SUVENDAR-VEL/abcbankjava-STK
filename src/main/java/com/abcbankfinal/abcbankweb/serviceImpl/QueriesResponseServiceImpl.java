package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.Queries;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.QueriesRepository;
import com.abcbankfinal.abcbankweb.repository.UserRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.QueriesResponseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueriesResponseServiceImpl
        implements QueriesResponseService {

    private final QueriesRepository queriesRepo;
    private final AccountRepository accountRepo;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<QueriesResponseDto> saveQuery(
            QueriesSaveDto dto) {

        Account account =
                accountRepo.findById(dto.getAccountNumber())
                        .orElseThrow(() ->
                                new RuntimeException("Account not found"));

        Queries entity = new Queries();
        entity.setCustomerQuery(dto.getCustomerQuery());
        entity.setQueryRaisedDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setAccount(account);

        queriesRepo.save(entity);

        return new ApiResponse<>(
                true,
                "Query submitted successfully",
                null
        );
    }

    @Override
    public ApiResponse<List<QueriesResponseDto>>
    getByAccountNumber(Long accountNumber) {

        List<Queries> list =
                queriesRepo.findQueriesByAccountNumber(accountNumber);

        return new ApiResponse<>(
                true,
                "Queries fetched successfully",
                list.stream()
                        .map(this::mapToDto)
                        .toList()
        );
    }

    @Override
    public ApiResponse<PageResponse<QueriesResponseDto>>
    getAllQueries(QueriesListRequestDto request) {

        Pageable pageable =
                PageRequest.of(
                        request.getPage(),
                        request.getSize()
                );

        Page<Queries> page =
                queriesRepo.findAllWithCustomer(
                        request.getStatus(),
                        pageable
                );

        List<QueriesResponseDto> content =
                page.stream()
                        .map(this::mapToDto)
                        .toList();

        PageResponse<QueriesResponseDto> pageResponse =
                new PageResponse<>(
                        content,
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                );

        return new ApiResponse<>(
                true,
                "Queries list fetched successfully",
                pageResponse
        );
    }

    @Override
    public ApiResponse<QueriesResponseDto>
    getQueryById(Long queryId) {

        Queries query =
                queriesRepo.findById(queryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Query not found with ID: "
                                                + queryId));

        return new ApiResponse<>(
                true,
                "Query fetched successfully",
                mapToDto(query)
        );
    }

    @Transactional
    @Override
    public ApiResponse<String> updateQueryStatus(
            Long queryId,
            QueriesUpdateRequestDto request) {

        Queries query =
                queriesRepo.findById(queryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Query not found with ID: "
                                                + queryId));

        User user =
                userRepository.findById(request.getApprovedById())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        query.setQueryApprovedBy(
                Math.toIntExact(user.getUserId()));
        query.setQueryApprovedDate(LocalDate.now());
        query.setQueryResponse(request.getRemarks());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            query.setStatus("Approved");
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            query.setStatus("Rejected");
        } else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        return new ApiResponse<>(
                true,
                "Query status updated successfully",
                null
        );
    }

    @Override
    public ApiResponse<RequestCountDto> getQueriesCounts() {

        RequestCountDto dto = new RequestCountDto();
        dto.setTotal(queriesRepo.count());
        dto.setApproved(queriesRepo.countByStatusIgnoreCase("APPROVED"));
        dto.setRejected(queriesRepo.countByStatusIgnoreCase("REJECTED"));
        dto.setPending(queriesRepo.countByStatusIgnoreCase("PENDING"));

        return new ApiResponse<>(
                true,
                "Queries request counts fetched successfully",
                dto
        );
    }

    private QueriesResponseDto mapToDto(Queries q) {

        QueriesResponseDto dto =
                new QueriesResponseDto();

        dto.setQueriesId(q.getQueriesId());
        dto.setCustomerQuery(q.getCustomerQuery());
        dto.setQueryRaisedDate(q.getQueryRaisedDate());
        dto.setQueryResponse(q.getQueryResponse());
        dto.setQueryApprovedBy(q.getQueryApprovedBy());
        dto.setQueryApprovedDate(q.getQueryApprovedDate());
        dto.setStatus(q.getStatus());
        dto.setAccountNumber(
                q.getAccount().getAccountNumber());

        dto.setFullName(
                q.getAccount().getCustomer().getFirstName()
                        + " "
                        + q.getAccount().getCustomer().getLastName());

        dto.setMobileNumber(
                q.getAccount().getCustomer().getMobileNumber());

        dto.setCity(
                q.getAccount().getCustomer().getCity());

        dto.setEmail(
                q.getAccount().getCustomer().getEmail());

        if (q.getQueryApprovedBy() != null) {

            User user =
                    userRepository.findById(
                                    Long.valueOf(q.getQueryApprovedBy()))
                            .orElse(null);

            if (user != null) {
                dto.setApprovedByName(
                        user.getFirstName() + " "
                                + user.getLastName());
            }
        }

        return dto;
    }
}
package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.ChequeListRequestDTO;
import com.abcbankfinal.abcbankweb.dto.ChequeRequestDto;
import com.abcbankfinal.abcbankweb.dto.ChequeUpdateRequestDTO;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.ChequeRequest;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.ChequeRequestRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.ChequeRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChequeRequestServiceImpl implements ChequeRequestService {

    private final ChequeRequestRepository chequeRequestRepository;
    private final AccountRepository accountRepository;

    public ChequeRequestServiceImpl(ChequeRequestRepository chequeRequestRepository, AccountRepository accountRepository) {
        this.chequeRequestRepository = chequeRequestRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public ApiResponse<ChequeRequestDto> saveChequeRequest(
            ChequeRequestDto dto) {

        Account account = accountRepository
                .findById(dto.getAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        ChequeRequest request = new ChequeRequest();
        request.setNoOfLeaves(dto.getNoOfLeaves());
        request.setRequestedDate(LocalDate.now());
        request.setStatus("Pending");
        request.setAccount(account);

        ChequeRequest saved =
                chequeRequestRepository.save(request);

        ChequeRequestDto responseDto =
                new ChequeRequestDto();

        responseDto.setChequeRequestId(
                saved.getChequeRequestId());
        responseDto.setNoOfLeaves(
                saved.getNoOfLeaves());
        responseDto.setRequestedDate(
                saved.getRequestedDate());
        responseDto.setStatus(saved.getStatus());
        responseDto.setAccountNumber(
                saved.getAccount().getAccountNumber());

        return new ApiResponse<>(
                true,
                "Cheque request submitted successfully",
                responseDto
        );
    }

    @Override
    public ApiResponse<List<ChequeRequestDto>>
    getByAccountNumber(Long accountNumber) {

        List<ChequeRequestDto> list =
                chequeRequestRepository
                        .findByAccount_AccountNumber(accountNumber)
                        .stream()
                        .map(req -> {

                            ChequeRequestDto dto =
                                    new ChequeRequestDto();

                            dto.setChequeRequestId(
                                    req.getChequeRequestId());
                            dto.setNoOfLeaves(
                                    req.getNoOfLeaves());
                            dto.setRequestedDate(
                                    req.getRequestedDate());
                            dto.setApprovedBy(
                                    req.getApprovedBy());
                            dto.setApprovedDate(
                                    req.getApprovedDate());
                            dto.setStatus(req.getStatus());
                            dto.setRemarks(req.getRemarks());
                            dto.setAccountNumber(
                                    req.getAccount()
                                            .getAccountNumber());

                            return dto;
                        })
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Cheque requests fetched successfully",
                list
        );
    }

    @Override
    public ApiResponse<Page<ChequeRequestDto>> getAllChequeRequests(
            ChequeListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestedDate").descending()
        );

        Page<ChequeRequest> pageResult;

        if (request.getStatus() == null ||
                request.getStatus().isBlank()) {

            pageResult = chequeRequestRepository.findAll(pageable);
        } else {

            pageResult = chequeRequestRepository.findByStatus(
                    request.getStatus(),
                    pageable);
        }

        Page<ChequeRequestDto> response =
                pageResult.map(req -> {
                    ChequeRequestDto dto = new ChequeRequestDto();
                    dto.setChequeRequestId(req.getChequeRequestId());
                    dto.setNoOfLeaves(req.getNoOfLeaves());
                    dto.setRequestedDate(req.getRequestedDate());
                    dto.setApprovedBy(req.getApprovedBy());
                    dto.setApprovedDate(req.getApprovedDate());
                    dto.setStatus(req.getStatus());
                    dto.setRemarks(req.getRemarks());
                    dto.setAccountNumber(
                            req.getAccount().getAccountNumber());
                    return dto;
                });

        return new ApiResponse<>(
                true,
                "Cheque request list fetched successfully",
                response
        );
    }

    @Override
    public ApiResponse<ChequeRequestDto> getChequeById(Integer id) {

        ChequeRequest req = chequeRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cheque request not found: " + id));

        ChequeRequestDto dto = new ChequeRequestDto();
        dto.setChequeRequestId(req.getChequeRequestId());
        dto.setNoOfLeaves(req.getNoOfLeaves());
        dto.setRequestedDate(req.getRequestedDate());
        dto.setApprovedBy(req.getApprovedBy());
        dto.setApprovedDate(req.getApprovedDate());
        dto.setStatus(req.getStatus());
        dto.setRemarks(req.getRemarks());
        dto.setAccountNumber(
                req.getAccount().getAccountNumber());

        return new ApiResponse<>(
                true,
                "Cheque request fetched successfully",
                dto
        );
    }

    @Override
    @Transactional
    public ApiResponse<String> updateChequeStatus(
            Integer id,
            ChequeUpdateRequestDTO request) {

        ChequeRequest cheque = chequeRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cheque request not found: " + id));

        cheque.setApprovedBy(1);
        cheque.setApprovedDate(LocalDate.now());

        if ("Approved".equalsIgnoreCase(request.getAction())) {

            cheque.setStatus("Approved");
            cheque.setRemarks(null);

        } else if ("Rejected".equalsIgnoreCase(request.getAction())) {

            cheque.setStatus("Rejected");
            cheque.setRemarks(request.getRemarks());

        } else {
            throw new RuntimeException(
                    "Invalid action. Use Approved or Rejected");
        }

        chequeRequestRepository.save(cheque);

        return new ApiResponse<>(
                true,
                "Cheque request status updated successfully",
                null
        );
    }
}
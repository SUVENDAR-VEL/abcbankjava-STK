package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.*;
import com.abcbankfinal.abcbankweb.repository.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.ChequeRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChequeRequestServiceImpl
        implements ChequeRequestService {

    private final ChequeRequestRepository chequeRepo;
    private final AccountRepository accountRepo;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<ChequeRequestDto>
    saveChequeRequest(ChequeRequestDto dto) {

        Account account = accountRepo.findById(
                        dto.getAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        ChequeRequest entity = new ChequeRequest();
        entity.setNoOfLeaves(dto.getNoOfLeaves());
        entity.setRequestedDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setAccount(account);

        chequeRepo.save(entity);

        return new ApiResponse<>(
                true,
                "Cheque request submitted successfully",
                null
        );
    }

    @Override
    public ApiResponse<List<ChequeRequestDto>>
    getByAccountNumber(Long accountNumber) {

        List<Object[]> results =
                chequeRepo.findChequeRequestOptimized(accountNumber);

        List<ChequeRequestDto> list =
                results.stream()
                        .map(this::mapFromCustomQuery)
                        .toList();

        return new ApiResponse<>(
                true,
                "Cheque requests fetched successfully",
                list
        );
    }

    @Override
    public ApiResponse<PageResponse<ChequeRequestDto>>
    getAllChequeRequests(ChequeListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        String statusFilter =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? null
                        : request.getStatus();

        Page<ChequeRequest> page =
                chequeRepo.findAllWithCustomer(
                        statusFilter,
                        pageable);

        List<ChequeRequestDto> content =
                page.stream()
                        .map(this::mapEntityToDto)
                        .toList();

        PageResponse<ChequeRequestDto> response =
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
                "Cheque request list fetched successfully",
                response
        );
    }

    @Override
    public ApiResponse<ChequeRequestDto>
    getChequeById(Integer id) {

        ChequeRequest req = chequeRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cheque request not found with ID: " + id));

        return new ApiResponse<>(
                true,
                "Cheque request fetched successfully",
                mapEntityToDto(req)
        );
    }

    @Transactional
    @Override
    public ApiResponse<String>
    updateChequeStatus(Integer id,
                       ChequeUpdateRequestDTO request) {

        ChequeRequest cheque =
                chequeRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Cheque not found"));

        User user = userRepository.findById(
                        request.getApprovedById())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        cheque.setApprovedBy(
                Math.toIntExact(user.getUserId()));
        cheque.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            cheque.setStatus("Approved");
            cheque.setRemarks(null);
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            cheque.setStatus("Rejected");
            cheque.setRemarks(request.getRemarks());
        } else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        return new ApiResponse<>(
                true,
                "Cheque request status updated successfully",
                null
        );
    }

    @Override
    public ApiResponse<RequestCountDto>
    getChequeRequestCounts() {

        RequestCountDto dto = new RequestCountDto();

        dto.setTotal(chequeRepo.count());
        dto.setApproved(
                chequeRepo.countByStatusIgnoreCase("APPROVED"));
        dto.setRejected(
                chequeRepo.countByStatusIgnoreCase("REJECTED"));
        dto.setPending(
                chequeRepo.countByStatusIgnoreCase("PENDING"));

        return new ApiResponse<>(
                true,
                "Cheque request counts fetched successfully",
                dto
        );
    }

    private ChequeRequestDto
    mapEntityToDto(ChequeRequest req) {

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

        var customer = req.getAccount().getCustomer();

        dto.setFullName(
                customer.getFirstName()
                        + " " +
                        customer.getLastName());

        dto.setMobileNumber(customer.getMobileNumber());
        dto.setCity(customer.getCity());
        dto.setEmail(customer.getEmail());

        return dto;
    }

    private ChequeRequestDto
    mapFromCustomQuery(Object[] obj) {

        ChequeRequestDto dto = new ChequeRequestDto();

        dto.setChequeRequestId((Integer) obj[0]);
        dto.setNoOfLeaves((Integer) obj[1]);
        dto.setRequestedDate((LocalDate) obj[2]);
        dto.setApprovedBy((Integer) obj[3]);
        dto.setApprovedDate((LocalDate) obj[4]);
        dto.setStatus((String) obj[5]);
        dto.setRemarks((String) obj[6]);
        dto.setAccountNumber((Long) obj[7]);

        dto.setFullName(obj[8] + " " + obj[9]);
        dto.setMobileNumber((String) obj[10]);
        dto.setCity((String) obj[11]);
        dto.setEmail((String) obj[12]);

        if (obj[13] != null) {
            dto.setApprovedByName(
                    obj[13] + " " + obj[14]);
        }

        return dto;
    }
}
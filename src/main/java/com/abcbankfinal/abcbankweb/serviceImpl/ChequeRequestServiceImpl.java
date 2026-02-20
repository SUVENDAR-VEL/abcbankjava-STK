package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.ChequeRequest;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.ChequeRequestRepository;
import com.abcbankfinal.abcbankweb.repository.UserRepository;
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
public class ChequeRequestServiceImpl implements ChequeRequestService {

    private final ChequeRequestRepository chequeRepo;
    private final AccountRepository accountRepo;
    private final UserRepository userRepository;

    // -------------------------------------------------------
    // SAVE
    // -------------------------------------------------------

    @Override
    public ApiResponse<ChequeRequestDto> saveChequeRequest(
            ChequeRequestDto dto) {

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

    // -------------------------------------------------------
    // LIST BY ACCOUNT NUMBER
    // -------------------------------------------------------

    @Override
    public ApiResponse<List<ChequeRequestDto>>
    getByAccountNumber(Long accountNumber) {

        List<Object[]> results =
                chequeRepo.findChequeRequestOptimized(accountNumber);

        List<ChequeRequestDto> list = results.stream()
                .map(obj -> {

                    ChequeRequestDto dto = new ChequeRequestDto();

                    dto.setChequeRequestId((Integer) obj[0]);
                    dto.setNoOfLeaves((Integer) obj[1]);
                    dto.setRequestedDate((LocalDate) obj[2]);
                    dto.setApprovedBy((Integer) obj[3]);
                    dto.setApprovedDate((LocalDate) obj[4]);
                    dto.setStatus((String) obj[5]);
                    dto.setRemarks((String) obj[6]);
                    dto.setAccountNumber((Long) obj[7]);

                    String firstName = (String) obj[8];
                    String lastName = (String) obj[9];
                    dto.setFullName(firstName + " " + lastName);

                    dto.setMobileNumber((String) obj[10]);
                    dto.setCity((String) obj[11]);
                    dto.setEmail((String) obj[12]);

                    String adminFirstName = (String) obj[13];
                    String adminLastName = (String) obj[14];

                    if (adminFirstName != null) {
                        dto.setApprovedByName(
                                adminFirstName + " " + adminLastName);
                    }

                    return dto;
                })
                .toList();

        return new ApiResponse<>(
                true,
                "Cheque requests fetched successfully",
                list
        );
    }

    // -------------------------------------------------------
    // ADMIN LIST (PAGINATION) — SAME AS LOST CARD
    // -------------------------------------------------------

    @Override
    public ApiResponse<PageResponse<ChequeRequestDto>>
    getAllChequeRequests(ChequeListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestedDate").descending()
        );

        Page<ChequeRequest> resultPage =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? chequeRepo.findAll(pageable)
                        : chequeRepo.findByStatus(
                        request.getStatus().toUpperCase(),
                        pageable);

        List<ChequeRequestDto> content =
                resultPage.stream()
                        .map(req -> {

                            String fullName =
                                    req.getAccount()
                                            .getCustomer()
                                            .getFirstName()
                                            + " " +
                                            req.getAccount()
                                                    .getCustomer()
                                                    .getLastName();

                            Integer approvedById = null;
                            String approvedByName = null;

                            if (req.getApprovedBy() != null) {

                                approvedById =
                                        req.getApprovedBy();

                                User user =
                                        userRepository.findById(
                                                        Long.valueOf(
                                                                approvedById))
                                                .orElse(null);

                                if (user != null) {
                                    approvedByName =
                                            user.getFirstName()
                                                    + " " +
                                                    user.getLastName();
                                }
                            }

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

                            dto.setFullName(fullName);
                            dto.setMobileNumber(
                                    req.getAccount()
                                            .getCustomer()
                                            .getMobileNumber());
                            dto.setCity(
                                    req.getAccount()
                                            .getCustomer()
                                            .getCity());
                            dto.setEmail(
                                    req.getAccount()
                                            .getCustomer()
                                            .getEmail());
                            dto.setApprovedByName(
                                    approvedByName);

                            return dto;
                        })
                        .toList();

        PageResponse<ChequeRequestDto> pageResponse =
                new PageResponse<>(
                        content,
                        resultPage.getNumber(),
                        resultPage.getSize(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages(),
                        resultPage.isLast()
                );

        return new ApiResponse<>(
                true,
                "Cheque request list fetched successfully",
                pageResponse
        );
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------

    @Override
    public ApiResponse<ChequeRequestDto>
    getChequeById(Integer id) {

        ChequeRequest req = chequeRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cheque request not found with ID: " + id));

        Integer approvedById = null;
        String approvedByName = null;

        if (req.getApprovedBy() != null) {

            approvedById = req.getApprovedBy();

            User user =
                    userRepository.findById(
                                    Long.valueOf(approvedById))
                            .orElse(null);

            if (user != null) {
                approvedByName =
                        user.getFirstName()
                                + " " +
                                user.getLastName();
            }
        }

        String fullName =
                req.getAccount().getCustomer().getFirstName()
                        + " " +
                        req.getAccount().getCustomer().getLastName();

        ChequeRequestDto dto =
                new ChequeRequestDto();

        dto.setChequeRequestId(req.getChequeRequestId());
        dto.setNoOfLeaves(req.getNoOfLeaves());
        dto.setRequestedDate(req.getRequestedDate());
        dto.setApprovedBy(req.getApprovedBy());
        dto.setApprovedDate(req.getApprovedDate());
        dto.setStatus(req.getStatus());
        dto.setRemarks(req.getRemarks());
        dto.setAccountNumber(
                req.getAccount().getAccountNumber());

        dto.setFullName(fullName);
        dto.setMobileNumber(
                req.getAccount().getCustomer()
                        .getMobileNumber());
        dto.setCity(
                req.getAccount().getCustomer()
                        .getCity());
        dto.setEmail(
                req.getAccount().getCustomer()
                        .getEmail());
        dto.setApprovedByName(approvedByName);

        return new ApiResponse<>(
                true,
                "Cheque request fetched successfully",
                dto
        );
    }

    // -------------------------------------------------------
    // UPDATE STATUS
    // -------------------------------------------------------

    @Transactional
    @Override
    public ApiResponse<String>
    updateChequeStatus(
            Integer id,
            ChequeUpdateRequestDTO request) {

        ChequeRequest cheque =
                chequeRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cheque request not found with ID: " + id));

        User user = userRepository.findById(
                        request.getApprovedById())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: "
                                        + request.getApprovedById()));

        cheque.setApprovedBy(
                Math.toIntExact(user.getUserId()));
        cheque.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(
                request.getAction())) {

            cheque.setStatus("Approved");
            cheque.setRemarks(null);

        } else if ("REJECT".equalsIgnoreCase(
                request.getAction())) {

            cheque.setStatus("Rejected");
            cheque.setRemarks(
                    request.getRemarks());

        } else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        chequeRepo.save(cheque);

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
                chequeRepo.countByStatusIgnoreCase(
                        "APPROVED"));

        dto.setRejected(
                chequeRepo.countByStatusIgnoreCase(
                        "REJECTED"));

        dto.setPending(
                chequeRepo.countByStatusIgnoreCase(
                        "PENDING"));

        return new ApiResponse<>(
                true,
                "Cheque request counts fetched successfully",
                dto
        );
    }
}
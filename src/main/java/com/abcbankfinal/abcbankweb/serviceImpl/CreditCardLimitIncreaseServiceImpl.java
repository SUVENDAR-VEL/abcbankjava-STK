package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.CardRepository;
import com.abcbankfinal.abcbankweb.repository.CreditCardLimitIncreaseRepository;
import com.abcbankfinal.abcbankweb.repository.UserRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.CreditCardLimitIncreaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    private final UserRepository userRepository;

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    saveCreditLimitIncrease(
            CreditLimitIncreaseSaveDto dto) {

        Account account =
                accountRepository.findById(
                                dto.getAccountNumber())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"));

        boolean hasCard =
                cardRepository.existsByAccountAccountNumber(
                        dto.getAccountNumber());

        if (!hasCard) {
            return new ApiResponse<>(
                    false,
                    "This account does not have any credit card",
                    null
            );
        }

        CreditCardLimitIncrease entity =
                new CreditCardLimitIncrease();

        entity.setRequestedLimit(
                dto.getRequestedLimit());
        entity.setRequestDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setAccount(account);

        repository.save(entity);

        return new ApiResponse<>(
                true,
                "Credit limit increase request submitted successfully",
                null
        );
    }

    @Override
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccountNumber(Long accountNumber) {

        List<CreditCardLimitIncrease> requests =
                repository.findByAccount_AccountNumberOrderByRequestDateDesc(
                        accountNumber);

        requests.sort(
                (a, b) -> b.getRequestDate()
                        .compareTo(a.getRequestDate())
        );

        List<CreditLimitIncreaseResponseDto> list =
                requests.stream()
                        .map(req -> {

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

                            String fullName =
                                    req.getAccount()
                                            .getCustomer()
                                            .getFirstName()
                                            + " " +
                                            req.getAccount()
                                                    .getCustomer()
                                                    .getLastName();

                            CreditLimitIncreaseResponseDto dto =
                                    new CreditLimitIncreaseResponseDto();

                            dto.setIncreaseCreditLimitId(
                                    req.getIncreaseCreditLimitId());
                            dto.setRequestedLimit(
                                    req.getRequestedLimit());
                            dto.setRequestDate(
                                    req.getRequestDate());
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

        return new ApiResponse<>(
                true,
                "Credit limit increase requests fetched successfully",
                list
        );
    }

    @Override
    public ApiResponse<PageResponse<CreditLimitIncreaseResponseDto>>
    getAllCreditLimitIncreases(
            CreditLimitIncreaseListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestDate").descending()
        );

        Page<CreditCardLimitIncrease> resultPage =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? repository.findAll(pageable)
                        : repository.findByStatus(
                        request.getStatus().toUpperCase(),
                        pageable);

        List<CreditLimitIncreaseResponseDto> content =
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

                            CreditLimitIncreaseResponseDto dto =
                                    new CreditLimitIncreaseResponseDto();

                            dto.setIncreaseCreditLimitId(
                                    req.getIncreaseCreditLimitId());
                            dto.setRequestedLimit(
                                    req.getRequestedLimit());
                            dto.setRequestDate(
                                    req.getRequestDate());
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

        PageResponse<CreditLimitIncreaseResponseDto> pageResponse =
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
                "Credit limit increase list fetched successfully",
                pageResponse
        );
    }

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    getCreditLimitIncreaseById(Long id) {

        CreditCardLimitIncrease req =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found with ID: " + id));

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

        CreditLimitIncreaseResponseDto dto =
                new CreditLimitIncreaseResponseDto();

        dto.setIncreaseCreditLimitId(
                req.getIncreaseCreditLimitId());
        dto.setRequestedLimit(
                req.getRequestedLimit());
        dto.setRequestDate(
                req.getRequestDate());
        dto.setApprovedBy(
                req.getApprovedBy());
        dto.setApprovedDate(
                req.getApprovedDate());
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
        dto.setApprovedByName(
                approvedByName);

        return new ApiResponse<>(
                true,
                "Credit limit increase request fetched successfully",
                dto
        );
    }

    @Transactional
    @Override
    public ApiResponse<String>
    updateCreditLimitIncreaseStatus(
            Long id,
            CreditLimitIncreaseUpdateDTO request) {

        CreditCardLimitIncrease entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found with ID: " + id));

        User user =
                userRepository.findById(
                                request.getApprovedById())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found with ID: "
                                                + request.getApprovedById()));

        entity.setApprovedBy(
                Math.toIntExact(user.getUserId()));
        entity.setApprovedDate(LocalDate.now());


        if ("APPROVE".equalsIgnoreCase(
                request.getAction())) {

            entity.setStatus("Approved");
            entity.setRemarks(null);

            Long accountNumber =
                    entity.getAccount()
                            .getAccountNumber();

            List<Card> cards =
                    cardRepository
                            .findByAccountAccountNumber(
                                    accountNumber);

            for (Card card : cards) {

                if (card.getCardType()
                        .getCardTypeName()
                        .equalsIgnoreCase("CREDIT")) {

                    card.setCurrentLimit(
                            entity.getRequestedLimit());

                    cardRepository.save(card);
                }
            }

        }

        else if ("REJECT".equalsIgnoreCase(
                request.getAction())) {

            entity.setStatus("Rejected");
            entity.setRemarks(
                    request.getRemarks());

        } else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        repository.save(entity);

        return new ApiResponse<>(
                true,
                "Credit limit increase status updated successfully",
                null
        );
    }

    @Override
    public ApiResponse<RequestCountDto>
    getCreditLimitIncreaseCounts() {

        RequestCountDto dto =
                new RequestCountDto();

        dto.setTotal(
                repository.count());

        dto.setApproved(
                repository.countByStatusIgnoreCase(
                        "APPROVED"));

        dto.setRejected(
                repository.countByStatusIgnoreCase(
                        "REJECTED"));

        dto.setPending(
                repository.countByStatusIgnoreCase(
                        "PENDING"));

        return new ApiResponse<>(
                true,
                "Credit limit request counts fetched successfully",
                dto
        );
    }
}
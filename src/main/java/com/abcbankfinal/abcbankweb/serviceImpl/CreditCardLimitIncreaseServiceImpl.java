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

    // ============================================================
    // SAVE REQUEST
    // ============================================================

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    saveCreditLimitIncrease(CreditLimitIncreaseSaveDto dto) {

        Account account = accountRepository.findById(
                        dto.getAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        List<Card> cards =
                cardRepository.findByAccountAccountNumberAndStatusIgnoreCase(
                        dto.getAccountNumber(),
                        "ACTIVE");

        if (cards.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "This account does not have any active card",
                    null);
        }

        // ✅ Get current credit card limit (SNAPSHOT)
        Double currentLimit = null;

        for (Card card : cards) {
            if (card.getCardType()
                    .getCardTypeName()
                    .equalsIgnoreCase("CREDIT")) {

                currentLimit = card.getCurrentLimit();
                break;
            }
        }

        if (currentLimit == null) {
            return new ApiResponse<>(
                    false,
                    "No active credit card found",
                    null);
        }

        CreditCardLimitIncrease entity =
                new CreditCardLimitIncrease();

        entity.setRequestedLimit(dto.getRequestedLimit());
        entity.setCurrentLimitAtRequest(currentLimit); // ✅ SNAPSHOT STORED
        entity.setRequestDate(LocalDate.now());
        entity.setStatus("PENDING");
        entity.setAccount(account);

        repository.save(entity);

        return new ApiResponse<>(
                true,
                "Credit limit increase request submitted successfully",
                null);
    }

    // ============================================================
    // GET BY ACCOUNT
    // ============================================================

    @Override
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccountNumber(Long accountNumber) {

        List<Object[]> results =
                repository.findCreditLimitIncreaseOptimized(accountNumber);

        List<CreditLimitIncreaseResponseDto> list =
                results.stream().map(obj -> {

                    CreditLimitIncreaseResponseDto dto =
                            new CreditLimitIncreaseResponseDto();

                    dto.setIncreaseCreditLimitId((Long) obj[0]);
                    dto.setRequestedLimit((Double) obj[1]);
                    dto.setCurrentLimitAtRequest((Double) obj[2]);
                    dto.setRequestDate((LocalDate) obj[3]);
                    dto.setApprovedBy((Integer) obj[4]);
                    dto.setApprovedDate((LocalDate) obj[5]);
                    dto.setRemarks((String) obj[6]);
                    dto.setStatus((String) obj[7]);
                    dto.setAccountNumber((Long) obj[8]);

                    String firstName = (String) obj[9];
                    String lastName = (String) obj[10];
                    dto.setFullName(firstName + " " + lastName);

                    dto.setMobileNumber((String) obj[11]);
                    dto.setCity((String) obj[12]);
                    dto.setEmail((String) obj[13]);

                    String adminFirst = (String) obj[14];
                    String adminLast = (String) obj[15];

                    if (adminFirst != null) {
                        dto.setApprovedByName(
                                adminFirst + " " + adminLast);
                    }

                    return dto;
                }).toList();

        return new ApiResponse<>(
                true,
                "Credit limit increase requests fetched successfully",
                list);
    }

    // ============================================================
    // ADMIN LIST
    // ============================================================

    @Override
    public ApiResponse<PageResponse<CreditLimitIncreaseResponseDto>>
    getAllCreditLimitIncreases(
            CreditLimitIncreaseListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestDate").descending());

        Page<CreditCardLimitIncrease> resultPage =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? repository.findAll(pageable)
                        : repository.findByStatus(
                        request.getStatus().toUpperCase(),
                        pageable);

        List<CreditLimitIncreaseResponseDto> content =
                resultPage.stream().map(req -> {

                    CreditLimitIncreaseResponseDto dto =
                            new CreditLimitIncreaseResponseDto();

                    dto.setIncreaseCreditLimitId(
                            req.getIncreaseCreditLimitId());
                    dto.setRequestedLimit(
                            req.getRequestedLimit());
                    dto.setCurrentLimitAtRequest(
                            req.getCurrentLimitAtRequest());
                    dto.setRequestDate(req.getRequestDate());
                    dto.setApprovedBy(req.getApprovedBy());
                    dto.setApprovedDate(req.getApprovedDate());
                    dto.setStatus(req.getStatus());
                    dto.setRemarks(req.getRemarks());
                    dto.setAccountNumber(
                            req.getAccount().getAccountNumber());

                    String fullName =
                            req.getAccount().getCustomer().getFirstName()
                                    + " " +
                                    req.getAccount().getCustomer().getLastName();

                    dto.setFullName(fullName);
                    dto.setMobileNumber(
                            req.getAccount().getCustomer().getMobileNumber());
                    dto.setCity(
                            req.getAccount().getCustomer().getCity());
                    dto.setEmail(
                            req.getAccount().getCustomer().getEmail());

                    if (req.getApprovedBy() != null) {
                        User user =
                                userRepository.findById(
                                                Long.valueOf(req.getApprovedBy()))
                                        .orElse(null);

                        if (user != null) {
                            dto.setApprovedByName(
                                    user.getFirstName()
                                            + " " +
                                            user.getLastName());
                        }
                    }

                    return dto;
                }).toList();

        PageResponse<CreditLimitIncreaseResponseDto> pageResponse =
                new PageResponse<>(
                        content,
                        resultPage.getNumber(),
                        resultPage.getSize(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages(),
                        resultPage.isLast());

        return new ApiResponse<>(
                true,
                "Credit limit increase list fetched successfully",
                pageResponse);
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    getCreditLimitIncreaseById(Long id) {

        CreditCardLimitIncrease req =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found with ID: " + id));

        CreditLimitIncreaseResponseDto dto =
                new CreditLimitIncreaseResponseDto();

        dto.setIncreaseCreditLimitId(
                req.getIncreaseCreditLimitId());
        dto.setRequestedLimit(
                req.getRequestedLimit());
        dto.setCurrentLimitAtRequest(
                req.getCurrentLimitAtRequest());
        dto.setRequestDate(req.getRequestDate());
        dto.setApprovedBy(req.getApprovedBy());
        dto.setApprovedDate(req.getApprovedDate());
        dto.setStatus(req.getStatus());
        dto.setRemarks(req.getRemarks());
        dto.setAccountNumber(
                req.getAccount().getAccountNumber());

        String fullName =
                req.getAccount().getCustomer().getFirstName()
                        + " " +
                        req.getAccount().getCustomer().getLastName();

        dto.setFullName(fullName);
        dto.setMobileNumber(
                req.getAccount().getCustomer().getMobileNumber());
        dto.setCity(
                req.getAccount().getCustomer().getCity());
        dto.setEmail(
                req.getAccount().getCustomer().getEmail());

        return new ApiResponse<>(
                true,
                "Credit limit increase request fetched successfully",
                dto);
    }

    // ============================================================
    // UPDATE STATUS
    // ============================================================

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
                                        "User not found"));

        entity.setApprovedBy(
                Math.toIntExact(user.getUserId()));
        entity.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("APPROVED");
            entity.setRemarks(null);

            Long accountNumber =
                    entity.getAccount().getAccountNumber();

            List<Card> cards =
                    cardRepository
                            .findByAccountAccountNumberAndStatusIgnoreCase(
                                    accountNumber,
                                    "ACTIVE");

            for (Card card : cards) {

                if (card.getCardType()
                        .getCardTypeName()
                        .equalsIgnoreCase("CREDIT")) {

                    // ✅ Only card limit changes
                    card.setCurrentLimit(
                            entity.getRequestedLimit());

                    cardRepository.save(card);
                }
            }

        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("REJECTED");
            entity.setRemarks(request.getRemarks());

        } else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        repository.save(entity);

        return new ApiResponse<>(
                true,
                "Credit limit increase status updated successfully",
                null);
    }

    // ============================================================
    // COUNT
    // ============================================================

    @Override
    public ApiResponse<RequestCountDto>
    getCreditLimitIncreaseCounts() {

        RequestCountDto dto = new RequestCountDto();

        dto.setTotal(repository.count());
        dto.setApproved(repository.countByStatusIgnoreCase("APPROVED"));
        dto.setRejected(repository.countByStatusIgnoreCase("REJECTED"));
        dto.setPending(repository.countByStatusIgnoreCase("PENDING"));

        return new ApiResponse<>(
                true,
                "Credit limit request counts fetched successfully",
                dto);
    }
}
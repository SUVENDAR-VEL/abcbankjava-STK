package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import com.abcbankfinal.abcbankweb.model.User;
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
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // ============================================================
    // SAVE CREDIT LIMIT INCREASE REQUEST
    // ============================================================

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    saveCreditLimitIncrease(CreditLimitIncreaseSaveDto dto) {

        List<Card> cards =
                cardRepository.findByAccountAccountNumberAndStatusIgnoreCase(
                        dto.getAccountNumber(),
                        "ACTIVE"
                );

        if (cards.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "This account does not have any active card",
                    null
            );
        }

        Card creditCard = cards.stream()
                .filter(card ->
                        "CREDIT".equalsIgnoreCase(
                                card.getCardType().getCardTypeName()))
                .findFirst()
                .orElse(null);

        if (creditCard == null) {
            return new ApiResponse<>(
                    false,
                    "No active credit card found",
                    null
            );
        }

        CreditCardLimitIncrease entity =
                new CreditCardLimitIncrease();

        entity.setRequestedLimit(dto.getRequestedLimit());
        entity.setCurrentLimitAtRequest(
                creditCard.getCurrentLimit());
        entity.setRequestDate(LocalDate.now());
        entity.setStatus("PENDING");
        entity.setCard(creditCard);

        repository.save(entity);

        return new ApiResponse<>(
                true,
                "Credit limit increase request submitted successfully",
                null
        );
    }

    // ============================================================
    // GET BY ACCOUNT NUMBER
    // ============================================================

    @Override
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByAccountNumber(Long accountNumber) {

        List<CreditCardLimitIncrease> list =
                repository.findAll().stream()
                        .filter(req ->
                                req.getCard()
                                        .getAccount()
                                        .getAccountNumber()
                                        .equals(accountNumber))
                        .toList();

        List<CreditLimitIncreaseResponseDto> response =
                list.stream()
                        .map(this::mapToDto)
                        .toList();

        return new ApiResponse<>(
                true,
                "Credit limit increase requests fetched successfully",
                response
        );
    }

    // ============================================================
    // ADMIN LIST WITH PAGINATION
    // ============================================================

    @Override
    public ApiResponse<PageResponse<CreditLimitIncreaseResponseDto>>
    getAllCreditLimitIncreases(
            CreditLimitIncreaseListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestDate").descending()
        );

        Page<CreditCardLimitIncrease> page =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? repository.findAll(pageable)
                        : repository.findByStatus(
                        request.getStatus().toUpperCase(),
                        pageable
                );

        List<CreditLimitIncreaseResponseDto> content =
                page.stream()
                        .map(this::mapToDto)
                        .toList();

        PageResponse<CreditLimitIncreaseResponseDto> pageResponse =
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
                "Credit limit increase list fetched successfully",
                pageResponse
        );
    }

    // ============================================================
    // GET BY REQUEST ID
    // ============================================================

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    getCreditLimitIncreaseById(Long id) {

        CreditCardLimitIncrease entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found with ID: " + id));

        return new ApiResponse<>(
                true,
                "Credit limit increase request fetched successfully",
                mapToDto(entity)
        );
    }

    // ============================================================
    // UPDATE STATUS (APPROVE / REJECT)
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
                                new RuntimeException("User not found"));

        entity.setApprovedBy(
                Math.toIntExact(user.getUserId()));
        entity.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("APPROVED");
            entity.setRemarks(null);

            Card card = entity.getCard();
            card.setCurrentLimit(
                    entity.getRequestedLimit());

            cardRepository.save(card);

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
                null
        );
    }

    // ============================================================
    // REQUEST COUNTS
    // ============================================================

    @Override
    public ApiResponse<RequestCountDto>
    getCreditLimitIncreaseCounts() {

        RequestCountDto dto = new RequestCountDto();

        dto.setTotal(repository.count());
        dto.setApproved(
                repository.countByStatusIgnoreCase("APPROVED"));
        dto.setRejected(
                repository.countByStatusIgnoreCase("REJECTED"));
        dto.setPending(
                repository.countByStatusIgnoreCase("PENDING"));

        return new ApiResponse<>(
                true,
                "Credit limit request counts fetched successfully",
                dto
        );
    }

    // ============================================================
    // COMMON DTO MAPPER
    // ============================================================

    private CreditLimitIncreaseResponseDto
    mapToDto(CreditCardLimitIncrease req) {

        CreditLimitIncreaseResponseDto dto =
                new CreditLimitIncreaseResponseDto();

        dto.setIncreaseCreditLimitId(
                req.getIncreaseCreditLimitId());
        dto.setRequestedLimit(req.getRequestedLimit());
        dto.setCurrentLimitAtRequest(
                req.getCurrentLimitAtRequest());
        dto.setRequestDate(req.getRequestDate());
        dto.setApprovedBy(req.getApprovedBy());
        dto.setApprovedDate(req.getApprovedDate());
        dto.setStatus(req.getStatus());
        dto.setRemarks(req.getRemarks());

        dto.setAccountNumber(
                req.getCard()
                        .getAccount()
                        .getAccountNumber());

        dto.setFullName(
                req.getCard()
                        .getAccount()
                        .getCustomer()
                        .getFirstName()
                        + " "
                        + req.getCard()
                        .getAccount()
                        .getCustomer()
                        .getLastName());

        dto.setMobileNumber(
                req.getCard()
                        .getAccount()
                        .getCustomer()
                        .getMobileNumber());

        dto.setCity(
                req.getCard()
                        .getAccount()
                        .getCustomer()
                        .getCity());

        dto.setEmail(
                req.getCard()
                        .getAccount()
                        .getCustomer()
                        .getEmail());

        if (req.getApprovedBy() != null) {

            User user =
                    userRepository.findById(
                                    Long.valueOf(req.getApprovedBy()))
                            .orElse(null);

            if (user != null) {
                dto.setApprovedByName(
                        user.getFirstName()
                                + " "
                                + user.getLastName());
            }
        }

        return dto;
    }
}
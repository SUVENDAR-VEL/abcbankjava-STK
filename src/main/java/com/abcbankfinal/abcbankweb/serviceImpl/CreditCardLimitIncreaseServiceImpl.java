package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.AccountFullDetailsProjection;
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
    private final AccountRepository accountRepository;

    // ======================================================
    // SAVE REQUEST
    // ======================================================

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    saveCreditLimitIncrease(CreditLimitIncreaseSaveDto dto) {

        Card card = cardRepository.findByCardNumber(dto.getCardNumber())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
            return new ApiResponse<>(false,
                    "Card is not active",
                    null);
        }

        CreditCardLimitIncrease entity = new CreditCardLimitIncrease();
        entity.setRequestedLimit(dto.getRequestedLimit());
        entity.setCurrentLimitAtRequest(card.getCurrentLimit());
        entity.setRequestDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setCard(card);

        repository.save(entity);

        return new ApiResponse<>(true,
                "Credit limit request submitted successfully",
                mapToDto(entity));
    }

    // ======================================================
    // LIST BY CARD
    // ======================================================

    @Override
    public ApiResponse<List<CreditLimitIncreaseResponseDto>>
    getByCardNumber(Long cardNumber) {

        List<CreditCardLimitIncrease> list =
                repository.findByCardNumberOptimized(cardNumber);

        return new ApiResponse<>(true,
                "Credit limit requests fetched successfully",
                list.stream().map(this::mapToDto).toList());
    }

    // ======================================================
    // ADMIN LIST
    // ======================================================

    @Override
    public ApiResponse<PageResponse<CreditLimitIncreaseResponseDto>>
    getAllCreditLimitIncreases(
            CreditLimitIncreaseListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("requestDate").descending());

        Page<CreditCardLimitIncrease> page =
                repository.findAllWithFilter(
                        request.getStatus(),
                        pageable);

        List<CreditLimitIncreaseResponseDto> content =
                page.stream().map(this::mapToDto).toList();

        return new ApiResponse<>(true,
                "Credit limit list fetched successfully",
                new PageResponse<>(
                        content,
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()));
    }

    // ======================================================
    // GET BY ID
    // ======================================================

    @Override
    public ApiResponse<CreditLimitIncreaseResponseDto>
    getCreditLimitIncreaseById(Long id) {

        CreditCardLimitIncrease entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        return new ApiResponse<>(true,
                "Request fetched successfully",
                mapToDto(entity));
    }

    // ======================================================
    // UPDATE STATUS
    // ======================================================

    @Transactional
    @Override
    public ApiResponse<String>
    updateCreditLimitIncreaseStatus(
            Long id,
            CreditLimitIncreaseUpdateDTO request) {

        CreditCardLimitIncrease entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        User admin = userRepository
                .findById(request.getApprovedById())
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        entity.setApprovedBy(Math.toIntExact(admin.getUserId()));
        entity.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("Approved");

            Card card = entity.getCard();
            card.setCurrentLimit(entity.getRequestedLimit());
            cardRepository.save(card);

        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("Rejected");
            entity.setRemarks(request.getRemarks());

        } else {
            throw new RuntimeException("Invalid action");
        }

        repository.save(entity);

        return new ApiResponse<>(true,
                "Status updated successfully",
                null);
    }

    // ======================================================
    // COUNT
    // ======================================================

    @Override
    public ApiResponse<RequestCountDto>
    getCreditLimitIncreaseCounts() {

        RequestCountDto dto = new RequestCountDto();
        dto.setTotal(repository.count());
        dto.setApproved(repository.countByStatusIgnoreCase("APPROVED"));
        dto.setRejected(repository.countByStatusIgnoreCase("REJECTED"));
        dto.setPending(repository.countByStatusIgnoreCase("PENDING"));

        return new ApiResponse<>(true,
                "Counts fetched successfully",
                dto);
    }

    // ======================================================
    // MAPPER (Using AccountFullDetailsProjection)
    // ======================================================

    private CreditLimitIncreaseResponseDto
    mapToDto(CreditCardLimitIncrease entity) {

        CreditLimitIncreaseResponseDto dto =
                new CreditLimitIncreaseResponseDto();

        dto.setIncreaseCreditLimitId(entity.getIncreaseCreditLimitId());
        dto.setRequestedLimit(entity.getRequestedLimit());
        dto.setCurrentLimitAtRequest(entity.getCurrentLimitAtRequest());
        dto.setRequestDate(entity.getRequestDate());
        dto.setApprovedBy(entity.getApprovedBy());
        dto.setApprovedDate(entity.getApprovedDate());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setCardNumber(entity.getCard().getCardNumber());

        Long accountNumber =
                entity.getCard().getAccount().getAccountNumber();

        dto.setAccountNumber(accountNumber);

        AccountFullDetailsProjection acc =
                accountRepository.findAccountFullDetails(accountNumber);

        if (acc != null) {
            dto.setFullName(acc.getFirstName() + " " + acc.getLastName());
            dto.setMobileNumber(acc.getMobileNumber());
            dto.setCity(acc.getCity());
            dto.setEmail(acc.getEmail());
        }

        if (entity.getApprovedBy() != null) {
            User user = userRepository
                    .findById(Long.valueOf(entity.getApprovedBy()))
                    .orElse(null);

            if (user != null) {
                dto.setApprovedByName(
                        user.getFirstName() + " " + user.getLastName());
            }
        }

        return dto;
    }
}
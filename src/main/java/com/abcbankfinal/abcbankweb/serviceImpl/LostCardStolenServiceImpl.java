package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.CardRepository;
import com.abcbankfinal.abcbankweb.repository.LostCardStolenRepository;
import com.abcbankfinal.abcbankweb.repository.UserRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.LostCardStolenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LostCardStolenServiceImpl
        implements LostCardStolenService {

    private final LostCardStolenRepository lostCardRepo;
    private final CardRepository cardRepo;
    private final UserRepository userRepo;

    // -------------------------------------------------------
    // SAVE
    // -------------------------------------------------------
    @Override
    public ApiResponse<String>
    saveLostCard(LostCardSaveRequestDTO dto) {

        Card card = cardRepo.findByCardNumber(
                        dto.getCardNumber())
                .orElseThrow(() ->
                        new RuntimeException("Card not found"));

        if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
            return new ApiResponse<>(
                    false,
                    "Card is not active",
                    null
            );
        }

        LostCardStolen entity =
                new LostCardStolen();

        entity.setLostCardNumber(
                dto.getCardNumber());
        entity.setLostCardStolenDate(
                dto.getLostCardStolenDate());
        entity.setCreatedDate(
                LocalDate.now());
        entity.setStatus("Pending");
        entity.setCard(card);

        lostCardRepo.save(entity);

        return new ApiResponse<>(
                true,
                "Lost card request submitted successfully",
                null
        );
    }

    // -------------------------------------------------------
    // CUSTOMER LIST – BY CARD NUMBER
    // -------------------------------------------------------
    @Override
    public ApiResponse<List<LostCardResponseDTO>>
    getLostCardsByCardNumber(Long cardNumber) {

        List<Object[]> results =
                lostCardRepo.findLostCardByCardNumber(
                        cardNumber);

        List<LostCardResponseDTO> list =
                results.stream()
                        .map(obj -> {

                            LostCardResponseDTO dto =
                                    new LostCardResponseDTO();

                            dto.setLostCardId((Long) obj[0]);
                            dto.setLostCardNumber((Long) obj[1]);
                            dto.setLostCardStolenDate(
                                    (LocalDate) obj[2]);
                            dto.setStatus((String) obj[3]);
                            dto.setRemarks((String) obj[4]);
                            dto.setCreatedDate(
                                    (LocalDate) obj[5]);
                            dto.setApprovedById(
                                    (Long) obj[6]);
                            dto.setApprovedDate(
                                    (LocalDate) obj[7]);
                            dto.setCardNumber(
                                    (Long) obj[8]);
                            dto.setAccountNumber(
                                    (Long) obj[9]);

                            // Customer
                            String first =
                                    (String) obj[10];
                            String last =
                                    (String) obj[11];

                            if (first != null) {
                                dto.setFullName(
                                        first + " " + last);
                            }

                            dto.setMobileNumber(
                                    (String) obj[12]);
                            dto.setCity(
                                    (String) obj[13]);
                            dto.setEmail(
                                    (String) obj[14]);

                            // Admin
                            String adminFirst =
                                    (String) obj[15];
                            String adminLast =
                                    (String) obj[16];

                            if (adminFirst != null) {
                                dto.setApprovedByName(
                                        adminFirst
                                                + " "
                                                + adminLast);
                            }

                            return dto;
                        })
                        .toList();

        return new ApiResponse<>(
                true,
                "Lost card list fetched successfully",
                list
        );
    }

    // -------------------------------------------------------
    // ADMIN LIST – PAGINATION
    // -------------------------------------------------------
    @Override
    public ApiResponse<PageResponse<LostCardResponseDTO>>
    getAllLostCards(LostCardListRequestDTO request) {

        Pageable pageable =
                PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        Sort.by("createdDate")
                                .descending()
                );

        Page<LostCardStolen> page =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? lostCardRepo.findAll(pageable)
                        : lostCardRepo.findByStatus(
                        request.getStatus()
                                .toUpperCase(),
                        pageable);

        List<LostCardResponseDTO> content =
                page.stream()
                        .map(entity -> {

                            LostCardResponseDTO dto =
                                    new LostCardResponseDTO();

                            dto.setLostCardId(
                                    entity.getLostCardId());
                            dto.setLostCardNumber(
                                    entity.getLostCardNumber());
                            dto.setLostCardStolenDate(
                                    entity.getLostCardStolenDate());
                            dto.setStatus(
                                    entity.getStatus());
                            dto.setRemarks(
                                    entity.getRemarks());
                            dto.setCreatedDate(
                                    entity.getCreatedDate());
                            dto.setApprovedDate(
                                    entity.getApprovedDate());

                            Card card =
                                    entity.getCard();

                            dto.setCardNumber(
                                    card.getCardNumber());
                            dto.setAccountNumber(
                                    card.getAccount()
                                            .getAccountNumber());

                            // Customer
                            dto.setFullName(
                                    card.getAccount()
                                            .getCustomer()
                                            .getFirstName()
                                            + " "
                                            +
                                            card.getAccount()
                                                    .getCustomer()
                                                    .getLastName());

                            dto.setMobileNumber(
                                    card.getAccount()
                                            .getCustomer()
                                            .getMobileNumber());
                            dto.setCity(
                                    card.getAccount()
                                            .getCustomer()
                                            .getCity());
                            dto.setEmail(
                                    card.getAccount()
                                            .getCustomer()
                                            .getEmail());

                            // Admin
                            if (entity.getApprovedBy() != null) {

                                dto.setApprovedById(
                                        entity.getApprovedBy()
                                                .getUserId());

                                dto.setApprovedByName(
                                        entity.getApprovedBy()
                                                .getFirstName()
                                                + " "
                                                +
                                                entity.getApprovedBy()
                                                        .getLastName());
                            }

                            return dto;
                        })
                        .toList();

        PageResponse<LostCardResponseDTO> pageResponse =
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
                "Lost card list fetched successfully",
                pageResponse
        );
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------
    @Override
    public ApiResponse<LostCardResponseDTO>
    getById(Long id) {

        LostCardStolen entity =
                lostCardRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lost card request not found"));

        Card card = entity.getCard();

        LostCardResponseDTO dto =
                new LostCardResponseDTO();

        dto.setLostCardId(
                entity.getLostCardId());
        dto.setLostCardNumber(
                entity.getLostCardNumber());
        dto.setLostCardStolenDate(
                entity.getLostCardStolenDate());
        dto.setStatus(
                entity.getStatus());
        dto.setRemarks(
                entity.getRemarks());
        dto.setCreatedDate(
                entity.getCreatedDate());
        dto.setApprovedDate(
                entity.getApprovedDate());
        dto.setCardNumber(
                card.getCardNumber());
        dto.setAccountNumber(
                card.getAccount()
                        .getAccountNumber());

        dto.setFullName(
                card.getAccount()
                        .getCustomer()
                        .getFirstName()
                        + " "
                        +
                        card.getAccount()
                                .getCustomer()
                                .getLastName());

        dto.setMobileNumber(
                card.getAccount()
                        .getCustomer()
                        .getMobileNumber());
        dto.setCity(
                card.getAccount()
                        .getCustomer()
                        .getCity());
        dto.setEmail(
                card.getAccount()
                        .getCustomer()
                        .getEmail());

        if (entity.getApprovedBy() != null) {
            dto.setApprovedById(
                    entity.getApprovedBy()
                            .getUserId());
            dto.setApprovedByName(
                    entity.getApprovedBy()
                            .getFirstName()
                            + " "
                            +
                            entity.getApprovedBy()
                                    .getLastName());
        }

        return new ApiResponse<>(
                true,
                "Lost card fetched successfully",
                dto
        );
    }

    // -------------------------------------------------------
    // UPDATE STATUS
    // -------------------------------------------------------
    @Transactional
    @Override
    public ApiResponse<String>
    updateLostCard(
            Long id,
            LostCardUpdateRequestDTO request) {

        LostCardStolen entity =
                lostCardRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lost card request not found"));

        User admin =
                userRepo.findById(
                                request.getApprovedById())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admin not found"));

        entity.setApprovedBy(admin);
        entity.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(
                request.getAction())) {

            entity.setStatus("Approved");

            Card card = entity.getCard();
            card.setStatus("Blocked");
            cardRepo.save(card);

        } else if ("REJECT".equalsIgnoreCase(
                request.getAction())) {

            entity.setStatus("Rejected");
            entity.setRemarks(
                    request.getRemarks());

        } else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        lostCardRepo.save(entity);

        return new ApiResponse<>(
                true,
                "Lost card status updated successfully",
                null
        );
    }

    // -------------------------------------------------------
    // COUNT
    // -------------------------------------------------------
    @Override
    public ApiResponse<RequestCountDto>
    getLostCardCounts() {

        RequestCountDto dto =
                new RequestCountDto();

        dto.setTotal(
                lostCardRepo.count());
        dto.setApproved(
                lostCardRepo.countByStatusIgnoreCase(
                        "APPROVED"));
        dto.setRejected(
                lostCardRepo.countByStatusIgnoreCase(
                        "REJECTED"));
        dto.setPending(
                lostCardRepo.countByStatusIgnoreCase(
                        "PENDING"));

        return new ApiResponse<>(
                true,
                "Lost card request counts fetched successfully",
                dto
        );
    }
}
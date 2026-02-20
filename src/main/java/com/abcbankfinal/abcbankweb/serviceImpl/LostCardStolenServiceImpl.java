package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.*;
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

    private final LostCardStolenRepository repository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // ======================================================
    // SAVE REQUEST
    // ======================================================

    @Override
    public ApiResponse<String> saveLostCard(
            LostCardSaveRequestDTO dto) {

        Card card = cardRepository
                .findByCardNumber(dto.getCardNumber())
                .orElseThrow(() ->
                        new RuntimeException("Card not found"));

        if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
            return new ApiResponse<>(false,
                    "Card is not active",
                    null);
        }

        LostCardStolen entity = new LostCardStolen();
        entity.setLostCardStolenDate(dto.getLostCardStolenDate());
        entity.setLostCardNumber(dto.getCardNumber());
        entity.setCreatedDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setCard(card);

        repository.save(entity);

        return new ApiResponse<>(true,
                "Lost card request submitted successfully",
                null);
    }

    // ======================================================
    // LIST BY CARD NUMBER
    // ======================================================

    @Override
    public ApiResponse<List<LostCardResponseDTO>>
    getLostCardsByCardNumber(Long cardNumber) {

        List<Object[]> results =
                repository.findLostCardByCardNumber(cardNumber);

        List<LostCardResponseDTO> list =
                results.stream().map(obj -> {

                    LostCardResponseDTO dto =
                            new LostCardResponseDTO();

                    dto.setLostCardId((Long) obj[0]);
                    dto.setLostCardNumber((Long) obj[1]);
                    dto.setLostCardStolenDate((LocalDate) obj[2]);
                    dto.setStatus((String) obj[3]);
                    dto.setRemarks((String) obj[4]);
                    dto.setCreatedDate((LocalDate) obj[5]);
                    dto.setApprovedById((Long) obj[6]);
                    dto.setApprovedDate((LocalDate) obj[7]);
                    dto.setCardNumber((Long) obj[8]);
                    dto.setAccountNumber((Long) obj[9]);

                    String firstName = (String) obj[10];
                    String lastName = (String) obj[11];

                    if (firstName != null)
                        dto.setFullName(firstName + " " + lastName);

                    dto.setMobileNumber((String) obj[12]);
                    dto.setCity((String) obj[13]);
                    dto.setEmail((String) obj[14]);

                    String adminFirst = (String) obj[15];
                    String adminLast = (String) obj[16];

                    if (adminFirst != null)
                        dto.setApprovedByName(
                                adminFirst + " " + adminLast);

                    return dto;

                }).toList();

        return new ApiResponse<>(true,
                "Lost card list fetched successfully",
                list);
    }

    // ======================================================
    // GET BY ID
    // ======================================================

    @Override
    public ApiResponse<LostCardResponseDTO> getById(Long id) {

        LostCardStolen entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Lost card request not found"));

        return new ApiResponse<>(true,
                "Lost card fetched successfully",
                mapToDto(entity));
    }

    // ======================================================
    // UPDATE STATUS (ADMIN APPROVE / REJECT)
    // ======================================================

    @Transactional
    @Override
    public ApiResponse<String> updateLostCard(
            Long id,
            LostCardUpdateRequestDTO request) {

        LostCardStolen entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Lost card request not found"));

        User admin = userRepository
                .findById(request.getApprovedById())
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        entity.setApprovedBy(admin);
        entity.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("Approved");

            // 🔴 BLOCK CARD WHEN APPROVED
            Card card = entity.getCard();
            card.setStatus("Blocked");
            cardRepository.save(card);

        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {

            entity.setStatus("Rejected");
            entity.setRemarks(request.getRemarks());

        } else {
            throw new RuntimeException("Invalid action");
        }

        repository.save(entity);

        return new ApiResponse<>(true,
                "Lost card status updated successfully",
                null);
    }

    // ======================================================
    // ADMIN LIST
    // ======================================================

    @Override
    public ApiResponse<PageResponse<LostCardResponseDTO>>
    getAllLostCards(LostCardListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdDate").descending());

        Page<LostCardStolen> page =
                (request.getStatus() == null ||
                        request.getStatus().isBlank())
                        ? repository.findAll(pageable)
                        : repository.findByStatus(
                        request.getStatus().toUpperCase(),
                        pageable);

        List<LostCardResponseDTO> content =
                page.stream().map(this::mapToDto).toList();

        return new ApiResponse<>(true,
                "Lost card list fetched successfully",
                new PageResponse<>(
                        content,
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()));
    }

    // ======================================================
    // COUNT
    // ======================================================

    @Override
    public ApiResponse<RequestCountDto> getLostCardCounts() {

        RequestCountDto dto = new RequestCountDto();
        dto.setTotal(repository.count());
        dto.setApproved(repository.countByStatusIgnoreCase("APPROVED"));
        dto.setRejected(repository.countByStatusIgnoreCase("REJECTED"));
        dto.setPending(repository.countByStatusIgnoreCase("PENDING"));

        return new ApiResponse<>(true,
                "Lost card request counts fetched successfully",
                dto);
    }

    // ======================================================
    // DTO MAPPER
    // ======================================================

    private LostCardResponseDTO mapToDto(LostCardStolen entity) {

        LostCardResponseDTO dto =
                new LostCardResponseDTO();

        dto.setLostCardId(entity.getLostCardId());
        dto.setLostCardNumber(entity.getLostCardNumber());
        dto.setLostCardStolenDate(entity.getLostCardStolenDate());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setApprovedDate(entity.getApprovedDate());
        dto.setCardNumber(entity.getCard().getCardNumber());
        dto.setAccountNumber(
                entity.getCard().getAccount().getAccountNumber());

        if (entity.getApprovedBy() != null) {
            dto.setApprovedById(
                    entity.getApprovedBy().getUserId());
            dto.setApprovedByName(
                    entity.getApprovedBy().getFirstName()
                            + " "
                            + entity.getApprovedBy().getLastName());
        }

        return dto;
    }
}
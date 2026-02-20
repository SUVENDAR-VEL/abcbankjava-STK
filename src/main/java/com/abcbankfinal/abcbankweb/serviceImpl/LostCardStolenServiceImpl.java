package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.Card;
import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.LostCardStolenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LostCardStolenServiceImpl implements LostCardStolenService {

    private final LostCardStolenRepository lostCardRepo;
    private final AccountRepository accountRepo;
    private final CardRepository cardRepository;

    private final UserRepository userRepository;


    @Override
    public ApiResponse<String> saveLostCard(LostCardSaveRequestDTO dto) {

        Account account = accountRepo.findById(dto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        LostCardStolen entity = new LostCardStolen();
        entity.setLostCardStolenDate(dto.getLostCardStolenDate());
        entity.setLostCardNumber(dto.getLostCardNumber());
        entity.setCreatedDate(LocalDate.now());
        entity.setStatus("Pending");
        entity.setAccount(account);

        lostCardRepo.save(entity);

        return new ApiResponse<>(
                true,
                "Lost card request submitted successfully",
                null
        );
    }


    @Override
    public ApiResponse<List<LostCardResponseDTO>>
    getLostCardsByAccountNumber(Long accountNumber) {

        List<Object[]> results =
                lostCardRepo.findLostCardOptimized(accountNumber);

        List<LostCardResponseDTO> list =
                results.stream()
                        .map(obj -> {

                            LostCardResponseDTO dto =
                                    new LostCardResponseDTO();

                            dto.setLostCardId((Long) obj[0]);
                            dto.setLostCardNumber((Long) obj[1]);
                            dto.setLostCardStolenDate((LocalDate) obj[2]);
                            dto.setStatus((String) obj[3]);
                            dto.setRemarks((String) obj[4]);
                            dto.setAccountNumber((Long) obj[5]);
                            dto.setCreatedDate((LocalDate) obj[6]);
                            dto.setApprovedById((Long) obj[7]);
                            dto.setApprovedDate((LocalDate) obj[8]);

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
                        })
                        .toList();

        return new ApiResponse<>(
                true,
                "Lost card list fetched successfully",
                list
        );
    }
    @Override
    public ApiResponse<PageResponse<LostCardResponseDTO>> getAllLostCards(LostCardListRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdDate").descending()
        );

        Page<LostCardStolen> resultPage =
                (request.getStatus() == null || request.getStatus().isBlank())
                        ? lostCardRepo.findAll(pageable)
                        : lostCardRepo.findByStatus(request.getStatus().toUpperCase(), pageable);

        List<LostCardResponseDTO> content = resultPage.stream()
                .map(lc -> {

                    String fullName =
                            lc.getAccount().getCustomer().getFirstName() + " " +
                                    lc.getAccount().getCustomer().getLastName();

                    Long approvedById = null;
                    String approvedByName = null;

                    if (lc.getApprovedBy() != null) {
                        approvedById = lc.getApprovedBy().getUserId();
                        approvedByName =
                                lc.getApprovedBy().getFirstName() + " " +
                                        lc.getApprovedBy().getLastName();
                    }

                    return new LostCardResponseDTO(
                            lc.getLostCardId(),
                            lc.getLostCardNumber(),
                            lc.getLostCardStolenDate(),
                            lc.getStatus(),
                            lc.getRemarks(),
                            lc.getAccount().getAccountNumber(),
                            lc.getCreatedDate(),
                            approvedById,
                            lc.getApprovedDate(),
                            fullName,
                            lc.getAccount().getCustomer().getMobileNumber(),
                            lc.getAccount().getCustomer().getCity(),
                            lc.getAccount().getCustomer().getEmail(),
                            approvedByName
                    );
                })
                .toList();

        PageResponse<LostCardResponseDTO> pageResponse =
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
                "Lost card list fetched successfully",
                pageResponse
        );
    }

    @Override
    public ApiResponse<LostCardResponseDTO> getById(Long id) {

        LostCardStolen lostCard = lostCardRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lost Card not found with ID: " + id)
                );

        Long approvedById = null;
        String approvedByName = null;

        if (lostCard.getApprovedBy() != null) {
            approvedById = lostCard.getApprovedBy().getUserId();
            approvedByName =
                    lostCard.getApprovedBy().getFirstName() + " " +
                            lostCard.getApprovedBy().getLastName();
        }

        String fullName =
                lostCard.getAccount().getCustomer().getFirstName() + " " +
                        lostCard.getAccount().getCustomer().getLastName();

        LostCardResponseDTO response = new LostCardResponseDTO(
                lostCard.getLostCardId(),
                lostCard.getLostCardNumber(),
                lostCard.getLostCardStolenDate(),
                lostCard.getStatus(),
                lostCard.getRemarks(),
                lostCard.getAccount().getAccountNumber(),
                lostCard.getCreatedDate(),
                approvedById,
                lostCard.getApprovedDate(),
                fullName,
                lostCard.getAccount().getCustomer().getMobileNumber(),
                lostCard.getAccount().getCustomer().getCity(),
                lostCard.getAccount().getCustomer().getEmail(),
                approvedByName
        );

        return new ApiResponse<>(
                true,
                "Lost card fetched successfully",
                response
        );
    }



    @Transactional
    @Override
    public ApiResponse<String> updateLostCard(
            Long id,
            LostCardUpdateRequestDTO request) {

        LostCardStolen lostCard = lostCardRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lost Card not found with ID: " + id)
                );

        User user = userRepository.findById(
                        request.getApprovedById())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: "
                                        + request.getApprovedById())
                );

        lostCard.setApprovedBy(user);
        lostCard.setApprovedDate(LocalDate.now());

        if ("APPROVE".equalsIgnoreCase(
                request.getAction())) {

            lostCard.setStatus("Approved");
            lostCard.setRemarks(null);

            Long accountNumber =
                    lostCard.getAccount()
                            .getAccountNumber();

            List<Card> cards =
                    cardRepository
                            .findByAccountAccountNumberAndStatusIgnoreCase(
                                    accountNumber,
                                    "ACTIVE"
                            );

            if (cards.isEmpty()) {
                throw new RuntimeException(
                        "No card found for this account");
            }

            for (Card card : cards) {
                card.setStatus("Blocked");
            }

            cardRepository.saveAll(cards);
        }

        else if ("REJECT".equalsIgnoreCase(
                request.getAction())) {

            lostCard.setStatus("Rejected");
            lostCard.setRemarks(
                    request.getRemarks());

        }

        else {
            throw new RuntimeException(
                    "Invalid action. Use APPROVE or REJECT");
        }

        lostCardRepo.save(lostCard);

        return new ApiResponse<>(
                true,
                "Lost card status updated successfully",
                null
        );
    }

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
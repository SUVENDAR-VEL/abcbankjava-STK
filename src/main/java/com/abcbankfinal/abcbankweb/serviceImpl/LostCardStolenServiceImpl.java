package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.LostCardStolenRepository;
import com.abcbankfinal.abcbankweb.repository.UserRepository;
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
    public ApiResponse<List<LostCardResponseDTO>> getLostCardsByAccountNumber(Long accountNumber) {
        List<LostCardResponseDTO> list =
                lostCardRepo.findByAccount_AccountNumber(accountNumber)
                        .stream()
                        .map(lc -> new LostCardResponseDTO(
                                lc.getLostCardId(),
                                lc.getLostCardNumber(),
                                lc.getLostCardStolenDate(),
                                lc.getStatus(),
                                lc.getRemarks(),
                                lc.getAccount().getAccountNumber(),
                                lc.getCreatedDate(),
                                lc.getApprovedBy(),
                                lc.getApprovedDate(),lc.getAccount().getCustomer().getFirstName() + " " +
                                lc.getAccount().getCustomer().getLastName(),
                                lc.getAccount().getCustomer().getMobileNumber(),
                                lc.getAccount().getCustomer().getCity(),lc.getAccount().getCustomer().getEmail()

                        ))
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

        Page<LostCardStolen> resultPage;

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            resultPage = lostCardRepo.findAll(pageable);
        } else {
            resultPage = lostCardRepo.findByStatus(
                    request.getStatus().toUpperCase(),
                    pageable
            );
        }

        Page<LostCardResponseDTO> response =
                resultPage.map(lc -> new LostCardResponseDTO(
                        lc.getLostCardId(),
                        lc.getLostCardNumber(),
                        lc.getLostCardStolenDate(),
                        lc.getStatus(),
                        lc.getRemarks(),
                        lc.getAccount().getAccountNumber(),
                        lc.getCreatedDate(),
                        lc.getApprovedBy(),
                        lc.getApprovedDate(),
                        lc.getAccount().getCustomer().getFirstName() + " " +
                                lc.getAccount().getCustomer().getLastName(),
                        lc.getAccount().getCustomer().getMobileNumber(),
                        lc.getAccount().getCustomer().getCity(),lc.getAccount().getCustomer().getEmail()
                ));

        PageResponse<LostCardResponseDTO> pageResponse =
                new PageResponse<>(
                        response.getContent(),
                        response.getNumber(),
                        response.getSize(),
                        response.getTotalElements(),
                        response.getTotalPages(),
                        response.isLast()
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

        LostCardResponseDTO response = new LostCardResponseDTO(
                lostCard.getLostCardId(),
                lostCard.getLostCardNumber(),
                lostCard.getLostCardStolenDate(),
                lostCard.getStatus(),
                lostCard.getRemarks(),
                lostCard.getAccount().getAccountNumber(),
                lostCard.getCreatedDate(),
                lostCard.getApprovedBy(),
                lostCard.getApprovedDate(), lostCard.getAccount().getCustomer().getFirstName() + " " +
                lostCard.getAccount().getCustomer().getLastName(),lostCard.getAccount().getCustomer().getMobileNumber(),
                lostCard.getAccount().getCustomer().getCity(),lostCard.getAccount().getCustomer().getEmail()

        );
        return new ApiResponse<>(
                true,
                "Lost card fetched successfully",
                response
        );
    }



//    @Transactional
//    public ApiResponse<String> updateLostCard(
//            Long id,
//            LostCardUpdateRequestDTO request) {
//
//        LostCardStolen lostCard = lostCardRepo.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Lost Card not found with ID: " + id)
//                );
//        lostCard.setApprovedBy(1L);
//        lostCard.setApprovedDate(LocalDate.now());
//
//        if ("Approved ".equalsIgnoreCase(request.getAction())) {
//            lostCard.setStatus("Approved");
//            lostCard.setRemarks(null);
//        }
//        else if ("Reject".equalsIgnoreCase(request.getAction())) {
//            lostCard.setStatus("Reject");
//            lostCard.setRemarks(request.getRemarks()); // optional
//        }
//        else {
//            throw new RuntimeException("Invalid action. Use APPROVE or REJECT");
//        }
//        lostCardRepo.save(lostCard);
//        return new ApiResponse<>(true, "Lost card Status updated successfully",null);
//    }


//    @Transactional
//    public ApiResponse<String> updateLostCard(
//            Long id,
//            LostCardUpdateRequestDTO request) {
//
//        LostCardStolen lostCard = lostCardRepo.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Lost Card not found with ID: " + id)
//                );
//
//        // 🔥 Fetch user with ID = 1
//        User user = userRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        lostCard.setApprovedBy(user);   // ✅ set User object
//        lostCard.setApprovedDate(LocalDate.now());
//
//        // ⚠ Remove extra space after Approved
//        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
//
//            lostCard.setStatus("Approved");
//            lostCard.setRemarks(null);
//
//        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
//
//            lostCard.setStatus("Rejected");
//            lostCard.setRemarks(request.getRemarks());
//
//        } else {
//            throw new RuntimeException("Invalid action. Use APPROVE or REJECT");
//        }
//
//        lostCardRepo.save(lostCard);
//
//        return new ApiResponse<>(
//                true,
//                "Lost card status updated successfully",
//                null
//        );
//    }



    @Transactional
    @Override
    public ApiResponse<String> updateLostCard(
            Long id,
            LostCardUpdateRequestDTO request) {

        // 1️⃣ Fetch Lost Card
        LostCardStolen lostCard = lostCardRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lost Card not found with ID: " + id)
                );

        // 2️⃣ Fetch Approved User using approvedById
        User user = userRepository.findById(request.getApprovedById())
                .orElseThrow(() ->
                        new RuntimeException("User not found with ID: " + request.getApprovedById())
                );

        // 3️⃣ Set Approved Details
        lostCard.setApprovedBy(user);
        lostCard.setApprovedDate(LocalDate.now());

        // 4️⃣ Update Status
        if ("APPROVE".equalsIgnoreCase(request.getAction())) {

            lostCard.setStatus("Approved");
            lostCard.setRemarks(null);

        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {

            lostCard.setStatus("Rejected");
            lostCard.setRemarks(request.getRemarks());

        } else {
            throw new RuntimeException("Invalid action. Use APPROVE or REJECT");
        }


        lostCardRepo.save(lostCard);

        return new ApiResponse<>(
                true,
                "Lost card status updated successfully",
                null
        );
    }


}


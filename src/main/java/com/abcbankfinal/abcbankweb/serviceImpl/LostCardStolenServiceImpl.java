package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.LostCardResponseDTO;
import com.abcbankfinal.abcbankweb.dto.LostCardSaveRequestDTO;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.LostCardStolenRepository;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.LostCardStolenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LostCardStolenServiceImpl implements LostCardStolenService {

    private final LostCardStolenRepository lostCardRepo;
    private final AccountRepository accountRepo;


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
                                lc.getAccount().getAccountNumber()
                        ))
                        .toList();

        return new ApiResponse<>(
                true,
                "Lost card list fetched successfully",
                list
        );
    }
}

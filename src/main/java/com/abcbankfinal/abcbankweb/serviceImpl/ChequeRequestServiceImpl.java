package com.abcbankfinal.abcbankweb.serviceImpl;

import com.abcbankfinal.abcbankweb.dto.ChequeRequestDto;
import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.model.ChequeRequest;
import com.abcbankfinal.abcbankweb.repository.AccountRepository;
import com.abcbankfinal.abcbankweb.repository.ChequeRequestRepository;
import com.abcbankfinal.abcbankweb.service.ChequeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChequeRequestServiceImpl implements ChequeRequestService {

    @Autowired
    private ChequeRequestRepository chequeRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ChequeRequestDto saveChequeRequest(ChequeRequestDto dto) {

        Account account = accountRepository.findById(dto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account Not Found"));

        ChequeRequest request = new ChequeRequest();
        request.setNoOfLeaves(dto.getNoOfLeaves());
        request.setRequestedDate(LocalDate.now());
        request.setStatus("Pending");
        request.setAccount(account);
        ChequeRequest saved = chequeRequestRepository.save(request);
        dto.setChequeRequestId(saved.getChequeRequestId());
        dto.setRequestedDate(saved.getRequestedDate());
        dto.setStatus(saved.getStatus());
        return dto;
    }

    @Override
    public List<ChequeRequestDto> getByAccountNumber(Long accountNumber) {

        List<ChequeRequest> requests =
                chequeRequestRepository.findByAccount_AccountNumber(accountNumber);

        return requests.stream().map(req -> {
            ChequeRequestDto dto = new ChequeRequestDto();
            dto.setChequeRequestId(req.getChequeRequestId());
            dto.setNoOfLeaves(req.getNoOfLeaves());
            dto.setRequestedDate(req.getRequestedDate());
            dto.setApprovedBy(req.getApprovedBy());
            dto.setApprovedDate(req.getApprovedDate());
            dto.setStatus(req.getStatus());
            dto.setRemarks(req.getRemarks());
            dto.setAccountNumber(req.getAccount().getAccountNumber());
            return dto;
        }).collect(Collectors.toList());
    }
}

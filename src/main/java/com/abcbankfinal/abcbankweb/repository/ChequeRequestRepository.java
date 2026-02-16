package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.ChequeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChequeRequestRepository
        extends JpaRepository<ChequeRequest, Integer> {

    List<ChequeRequest> findByAccount_AccountNumber(
            Long accountNumber);

    Page<ChequeRequest> findByStatus(
            String status,
            Pageable pageable);

    long countByStatusIgnoreCase(String status);
}
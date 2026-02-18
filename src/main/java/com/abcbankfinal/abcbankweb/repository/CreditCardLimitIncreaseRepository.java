package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardLimitIncreaseRepository
        extends JpaRepository<CreditCardLimitIncrease, Long> {

    List<CreditCardLimitIncrease>
    findByAccount_AccountNumberOrderByRequestDateDesc(
            Long accountNumber);

    Page<CreditCardLimitIncrease>
    findByStatus(String status, Pageable pageable);

    long countByStatusIgnoreCase(String status);
}
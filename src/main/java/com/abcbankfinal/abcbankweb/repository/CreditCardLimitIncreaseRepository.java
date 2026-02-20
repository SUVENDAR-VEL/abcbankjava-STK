package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.dto.CreditLimitIncreaseResponseDto;
import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditCardLimitIncreaseRepository
        extends JpaRepository<CreditCardLimitIncrease, Long> {

    @Query("""
SELECT 
    c.increaseCreditLimitId,
    c.requestedLimit,
    c.currentLimitAtRequest,
    c.requestDate,
    c.approvedBy,
    c.approvedDate,
    c.remarks,
    c.status,
    a.accountNumber,
    cust.firstName,
    cust.lastName,
    cust.mobileNumber,
    cust.city,
    cust.email,
    admin.firstName,
    admin.lastName
FROM CreditCardLimitIncrease c
JOIN c.account a
JOIN a.customer cust
LEFT JOIN User admin ON admin.userId = c.approvedBy
WHERE a.accountNumber = :accountNumber
ORDER BY c.requestDate DESC
""")
    List<Object[]> findCreditLimitIncreaseOptimized(Long accountNumber);

    Page<CreditCardLimitIncrease>
    findByStatus(String status, Pageable pageable);

    long countByStatusIgnoreCase(String status);
}
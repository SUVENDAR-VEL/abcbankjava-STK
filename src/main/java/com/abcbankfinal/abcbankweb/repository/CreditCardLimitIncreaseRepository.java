package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditCardLimitIncreaseRepository
        extends JpaRepository<CreditCardLimitIncrease, Long> {

    // ✅ FIXED JPQL (correct entity path)
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
            acc.accountNumber,
            cust.firstName,
            cust.lastName,
            cust.mobileNumber,
            cust.city,
            cust.email,
            admin.firstName,
            admin.lastName
        FROM CreditCardLimitIncrease c
        JOIN c.card card
        JOIN card.account acc
        JOIN acc.customer cust
        LEFT JOIN User admin ON admin.userId = c.approvedBy
        WHERE acc.accountNumber = :accountNumber
        ORDER BY c.requestDate DESC
    """)
    List<Object[]> findCreditLimitIncreaseOptimized(
            @Param("accountNumber") Long accountNumber
    );

    Page<CreditCardLimitIncrease>
    findByStatus(String status, Pageable pageable);

    long countByStatusIgnoreCase(String status);
}
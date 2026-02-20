package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditCardLimitIncreaseRepository
        extends JpaRepository<CreditCardLimitIncrease, Long> {

    // ✅ Optimized List by Card Number
    @Query("""
        SELECT c
        FROM CreditCardLimitIncrease c
        JOIN FETCH c.card cd
        JOIN FETCH cd.account acc
        JOIN FETCH acc.customer cust
        WHERE cd.cardNumber = :cardNumber
        ORDER BY c.requestDate DESC
    """)
    List<CreditCardLimitIncrease> findByCardNumberOptimized(
            @Param("cardNumber") Long cardNumber);


    // ✅ Optimized Admin List
    @Query("""
        SELECT c
        FROM CreditCardLimitIncrease c
        JOIN FETCH c.card cd
        JOIN FETCH cd.account acc
        JOIN FETCH acc.customer cust
        WHERE (:status IS NULL OR :status = '' OR c.status = :status)
    """)
    Page<CreditCardLimitIncrease> findAllWithFilter(
            @Param("status") String status,
            Pageable pageable);


    long countByStatusIgnoreCase(String status);
}
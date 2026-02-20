package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LostCardStolenRepository
        extends JpaRepository<LostCardStolen, Long> {

    @Query("""
        SELECT 
        c.lostCardId,
        c.lostCardNumber,
        c.lostCardStolenDate,
        c.status,
        c.remarks,
        c.createdDate,
        u.userId,
        c.approvedDate,
        cd.cardNumber,
        acc.accountNumber,
        cust.firstName,
        cust.lastName,
        cust.mobileNumber,
        cust.city,
        cust.email,
        u.firstName,
        u.lastName
        FROM LostCardStolen c
        LEFT JOIN c.card cd
        LEFT JOIN cd.account acc
        LEFT JOIN acc.customer cust
        LEFT JOIN c.approvedBy u
        WHERE cd.cardNumber = :cardNumber
        ORDER BY c.createdDate DESC
    """)
    List<Object[]> findLostCardByCardNumber(
            @Param("cardNumber") Long cardNumber
    );

    Page<LostCardStolen> findByStatus(String status, Pageable pageable);

    long countByStatusIgnoreCase(String status);
}
package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LostCardStolenRepository
        extends JpaRepository<LostCardStolen, Long> {

    @Query("""
        SELECT l
        FROM LostCardStolen l
        JOIN FETCH l.card cd
        JOIN FETCH cd.account acc
        JOIN FETCH acc.customer cust
        LEFT JOIN FETCH l.approvedBy admin
        WHERE cd.cardNumber = :cardNumber
        ORDER BY l.lostCardId DESC
    """)
    List<LostCardStolen> findLostCardByCardNumber(
            @Param("cardNumber") Long cardNumber
    );


    @Query(
            value = """
            SELECT l
            FROM LostCardStolen l
            JOIN FETCH l.card cd
            JOIN FETCH cd.account acc
            JOIN FETCH acc.customer cust
            LEFT JOIN FETCH l.approvedBy admin
            WHERE (:status IS NULL OR :status = '' OR UPPER(l.status) = UPPER(:status))
            ORDER BY l.lostCardId DESC
        """,
            countQuery = """
            SELECT COUNT(l)
            FROM LostCardStolen l
            WHERE (:status IS NULL OR :status = '' OR UPPER(l.status) = UPPER(:status))
        """
    )
    Page<LostCardStolen> findAllWithCustomer(
            @Param("status") String status,
            Pageable pageable
    );

    long countByStatusIgnoreCase(String status);
}
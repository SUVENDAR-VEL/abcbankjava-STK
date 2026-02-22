package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.Queries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QueriesRepository extends JpaRepository<Queries, Long> {

    @Query("""
        SELECT q
        FROM Queries q
        JOIN FETCH q.account a
        JOIN FETCH a.customer cust
        LEFT JOIN User admin
            ON admin.userId = q.queryApprovedBy
        WHERE a.accountNumber = :accountNumber
        ORDER BY q.queriesId DESC
    """)
    List<Queries> findQueriesByAccountNumber(
            @Param("accountNumber") Long accountNumber
    );


    @Query(
            value = """
            SELECT q
            FROM Queries q
            JOIN FETCH q.account a
            JOIN FETCH a.customer cust
            WHERE (:status IS NULL OR :status = '' OR UPPER(q.status) = UPPER(:status))
            ORDER BY q.queriesId DESC
        """,
            countQuery = """
            SELECT COUNT(q)
            FROM Queries q
            WHERE (:status IS NULL OR :status = '' OR UPPER(q.status) = UPPER(:status))
        """
    )
    Page<Queries> findAllWithCustomer(
            @Param("status") String status,
            Pageable pageable
    );

    long countByStatusIgnoreCase(String status);
}
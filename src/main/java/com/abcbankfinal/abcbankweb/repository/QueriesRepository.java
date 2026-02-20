package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.Queries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueriesRepository extends JpaRepository<Queries, Long> {

    @Query("""
    SELECT 
        q.queriesId,
        q.customerQuery,
        q.queryRaisedDate,
        q.queryResponse,
        q.queryApprovedBy,
        q.queryApprovedDate,
        q.status,
        a.accountNumber,
        cust.firstName,
        cust.lastName,
        cust.mobileNumber,
        cust.city,
        cust.email,
        admin.firstName,
        admin.lastName
    FROM Queries q
    JOIN q.account a
    JOIN a.customer cust
    LEFT JOIN User admin 
        ON admin.userId = q.queryApprovedBy
    WHERE a.accountNumber = :accountNumber
    ORDER BY q.queryRaisedDate DESC
""")
    List<Object[]> findQueriesOptimized(Long accountNumber);

    Page<Queries> findByStatus(String status, Pageable pageable);

    long countByStatusIgnoreCase(
            String status);
}
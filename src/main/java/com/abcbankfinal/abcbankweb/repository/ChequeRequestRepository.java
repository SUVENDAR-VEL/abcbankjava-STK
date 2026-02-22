package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.ChequeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChequeRequestRepository
        extends JpaRepository<ChequeRequest, Integer> {

    @Query("""
        SELECT 
            c.chequeRequestId,
            c.noOfLeaves,
            c.requestedDate,
            c.approvedBy,
            c.approvedDate,
            c.status,
            c.remarks,
            a.accountNumber,
            cust.firstName,
            cust.lastName,
            cust.mobileNumber,
            cust.city,
            cust.email,
            admin.firstName,
            admin.lastName
        FROM ChequeRequest c
        JOIN c.account a
        JOIN a.customer cust
        LEFT JOIN User admin ON admin.userId = c.approvedBy
        WHERE a.accountNumber = :accountNumber
        ORDER BY c.chequeRequestId DESC
    """)
    List<Object[]> findChequeRequestOptimized(
            @Param("accountNumber") Long accountNumber);


    @Query("""
        SELECT c FROM ChequeRequest c
        JOIN FETCH c.account a
        JOIN FETCH a.customer cust
        WHERE (:status IS NULL OR LOWER(c.status) = LOWER(:status))
        ORDER BY c.chequeRequestId DESC
    """)
    Page<ChequeRequest> findAllWithCustomer(
            @Param("status") String status,
            Pageable pageable);


    long countByStatusIgnoreCase(String status);
}
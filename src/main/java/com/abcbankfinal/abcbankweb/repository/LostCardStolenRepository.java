package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.dto.LostCardResponseDTO;
import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostCardStolenRepository
        extends JpaRepository<LostCardStolen, Long> {

    @Query("""
    SELECT 
        l.lostCardId,
        l.lostCardNumber,
        l.lostCardStolenDate,
        l.status,
        l.remarks,
        a.accountNumber,
        l.createdDate,
        admin.userId,
        l.approvedDate,
        cust.firstName,
        cust.lastName,
        cust.mobileNumber,
        cust.city,
        cust.email,
        admin.firstName,
        admin.lastName
    FROM LostCardStolen l
    JOIN l.account a
    JOIN a.customer cust
    LEFT JOIN l.approvedBy admin
    WHERE a.accountNumber = :accountNumber
    ORDER BY l.createdDate DESC
""")
    List<Object[]> findLostCardOptimized(Long accountNumber);


    Page<LostCardStolen>
    findByStatus(
            String status,
            Pageable pageable);

    long countByStatusIgnoreCase(
            String status);
}
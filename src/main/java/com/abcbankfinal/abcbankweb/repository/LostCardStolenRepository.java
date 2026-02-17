package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostCardStolenRepository
        extends JpaRepository<LostCardStolen, Long> {

    List<LostCardStolen>
    findByAccount_AccountNumberOrderByLostCardRequestDateDesc(
            Long accountNumber);

    Page<LostCardStolen>
    findByStatus(
            String status,
            Pageable pageable);

    long countByStatusIgnoreCase(
            String status);
}
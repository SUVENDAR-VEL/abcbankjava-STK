package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.Queries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueriesRepository extends JpaRepository<Queries, Long> {

    List<Queries> findByAccount_AccountNumber(Long accountNumber);

    Page<Queries> findByStatus(String status, Pageable pageable);

    long countByStatusIgnoreCase(
            String status);
}
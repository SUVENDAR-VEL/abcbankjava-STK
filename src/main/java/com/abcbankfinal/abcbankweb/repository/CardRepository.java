package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByAccountAccountNumberAndStatusIgnoreCase(
            Long accountNumber,
            String status
    );

    boolean existsByAccountAccountNumber(Long accountNumber);
}
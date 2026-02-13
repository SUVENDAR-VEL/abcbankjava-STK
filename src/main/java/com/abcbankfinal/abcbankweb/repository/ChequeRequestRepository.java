package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.ChequeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChequeRequestRepository extends JpaRepository<ChequeRequest, Integer> {

    List<ChequeRequest> findByAccount_AccountNumber(Long accountNumber);
}
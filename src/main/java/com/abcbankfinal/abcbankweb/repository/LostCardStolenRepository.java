package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.LostCardStolen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;


@Repository
public interface LostCardStolenRepository extends JpaRepository<LostCardStolen, Long> {

    List<LostCardStolen> findByAccount_AccountNumber(Long accountNumber);

}


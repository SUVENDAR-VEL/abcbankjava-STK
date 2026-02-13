package com.abcbankfinal.abcbankweb.repository;

import com.abcbankfinal.abcbankweb.model.CreditCardLimitIncrease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardLimitIncreaseRepository
        extends JpaRepository<CreditCardLimitIncrease, Long> {

    List<CreditCardLimitIncrease>
    findByAccountAccountNumber(Long accountNumber);

}

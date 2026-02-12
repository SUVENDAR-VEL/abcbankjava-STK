package com.abcbankfinal.abcbankweb.repository;


import com.abcbankfinal.abcbankweb.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerUserId(Long userId);
}

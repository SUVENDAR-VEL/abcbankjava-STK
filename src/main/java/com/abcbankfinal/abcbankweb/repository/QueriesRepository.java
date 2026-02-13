package com.abcbankfinal.abcbankweb.repository;


import com.abcbankfinal.abcbankweb.model.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueriesRepository extends JpaRepository<Queries, Long> {

    List<Queries> findByAccountAccountNumber(Long accountNumber);
}

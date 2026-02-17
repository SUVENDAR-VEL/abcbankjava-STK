package com.abcbankfinal.abcbankweb.repository;


import com.abcbankfinal.abcbankweb.model.Account;
import com.abcbankfinal.abcbankweb.service.UserAccountListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerUserId(Long userId);

    @Query(value = """
    SELECT 
        u.user_id AS userId,
        u.first_name AS firstName,
        u.last_name AS lastName,
        u.email AS email,
        u.mobile_number AS mobileNumber,
        u.city AS city,
        u.state AS state,
        u.role_id AS roleId,              
        a.account_number AS accountNumber,
        a.status AS accountStatus,
        at.account_type_name AS accountType
    FROM account a
    JOIN user u ON a.customer_id = u.user_id
    JOIN account_type at ON a.account_type_id = at.account_type_id
    WHERE (:status IS NULL OR a.status = :status)
      AND (:roleId IS NULL OR u.role_id = :roleId)
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM account a
    JOIN user u ON a.customer_id = u.user_id
    WHERE (:status IS NULL OR a.status = :status)
      AND (:roleId IS NULL OR u.role_id = :roleId)
    """,
            nativeQuery = true)
    Page<UserAccountListProjection> searchUsers(
            @Param("status") String status,
            @Param("roleId") Long roleId,
            Pageable pageable);


}

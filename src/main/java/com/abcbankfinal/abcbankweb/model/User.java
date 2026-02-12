package com.abcbankfinal.abcbankweb.model;


import lombok.Data;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "mobile_number", length = 12)
    private String mobileNumber;

    @Column(name = "alternative_number", length = 12)
    private String alternativeNumber;

    @Column(name = "email", length = 65)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 225)
    private String address;

    @Column(name = "city", length = 30)
    private String city;

    @Column(name = "state", length = 25)
    private String state;

    @Column(name = "country", length = 45)
    private String country;

    @Column(name = "pincode", length = 6)
    private String pincode;

    @Column(name = "pancard", length = 14)
    private String pancard;

    @Column(name = "aadhar", length = 12)
    private String aadhar;

    @Column(name = "password", length = 12)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}


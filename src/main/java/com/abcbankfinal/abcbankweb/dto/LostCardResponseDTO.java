package com.abcbankfinal.abcbankweb.dto;

import com.abcbankfinal.abcbankweb.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LostCardResponseDTO {

    private Long lostCardId;
    private Long lostCardNumber;
    private LocalDate lostCardStolenDate;
    private String status;
    private String remarks;
    private Long accountNumber;
    private LocalDate createdDate;
    private Long approvedById;
    private LocalDate approvedDate;
    private String fullName;
    private String mobileNumber;
    private String city;
    private String email;
    private String approvedByName;
}

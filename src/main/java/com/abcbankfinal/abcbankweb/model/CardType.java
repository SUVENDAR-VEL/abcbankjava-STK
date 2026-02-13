package com.abcbankfinal.abcbankweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "card_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_type_id")
    private Integer cardTypeId;

    @Column(name = "card_type_name", nullable = false)
    private String cardTypeName;

    @OneToMany(mappedBy = "cardType")
    private List<Card> cards;
}

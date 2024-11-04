package com.mthree.bankmthree.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currency;
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
    @ToString.Exclude
    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;
    @ManyToMany(mappedBy = "accounts")
    @ToString.Exclude
    private Set<UserGroup> userGroups;
    @ManyToMany
    @JoinTable(name = "account_services", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}
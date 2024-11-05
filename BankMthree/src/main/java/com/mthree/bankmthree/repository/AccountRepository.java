package com.mthree.bankmthree.repository;

import com.mthree.bankmthree.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCardNumber(String cardNumber);

    Optional<Account> findByCardNumber(String cardNumber);

    Optional<Account> findByCardNumberAndUserUsername(String cardNumber, String username);
}
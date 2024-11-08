package com.mthree.bankmthree.repository;

import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.entity.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCardNumber(String cardNumber);

    Optional<Account> findByCardNumber(String cardNumber);

    Optional<Account> findByCardNumberAndUser_Profile_Username(String cardNumber, String username);

    boolean existsByUserAndCurrency(User user, CurrencyType currency);

    Set<Account> findByUser(User user);
}
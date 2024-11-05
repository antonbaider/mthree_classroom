package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.account.AccountDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.enums.CurrencyType;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.exception.account.AccountBalanceNotZeroException;
import com.mthree.bankmthree.exception.account.AccountsNotFoundException;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.repository.UserRepository;
import com.mthree.bankmthree.util.CardNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CardNumberGenerator cardNumberGenerator;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, CardNumberGenerator cardNumberGenerator, UserMapper userMapper, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.cardNumberGenerator = cardNumberGenerator;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public Account createAndInitializeAccount(CurrencyType currency, User user) {
        log.info("Creating a new account for user {} with currency {}", user.getUsername(), currency);
        String cardNumber;
        int maxAttempts = 5;
        boolean isUnique = false;
        LocalDate creationDate = LocalDate.now();
        LocalDate expirationDate = creationDate.plusYears(5);
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            cardNumber = cardNumberGenerator.generateCardNumber();
            if (!accountRepository.existsByCardNumber(cardNumber)) {
                isUnique = true;
                break;
            }
        }
        if (!isUnique) {
            log.error("Unable to generate a unique card number after {} attempts", maxAttempts);
            throw new RuntimeException("Unable to generate a unique card number after " + maxAttempts + " attempts");
        }
        Account account = new Account();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        account.setCardNumber(cardNumberGenerator.generateCardNumber());
        account.setExpirationDate(expirationDate);

        log.info("Account created successfully for user {}", user.getUsername());
        return account;
    }

    @Transactional
    @CachePut(value = "accounts", key = "#user.username + '-' + #currency")
    public Account createAccount(User user, CurrencyType currency) {
        if (user.getAccounts().stream().anyMatch(account -> account.getCurrency() == currency)) {
            throw new IllegalArgumentException("Account with currency " + currency + " already exists.");
        }
        Account account = createAndInitializeAccount(currency, user);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Set<AccountDTO> getUserAccounts(User user) {
        return user.getAccounts().stream().map(userMapper::toAccountDTO).collect(Collectors.toSet());
    }

    @Transactional
    @CacheEvict(value = "userAccounts", key = "#username")
    public void closeAccount(String cardNumber, String username) {
        log.info("Closing account {} for user {}", cardNumber, username);
        Account account = (Account) accountRepository.findByCardNumberAndUserUsername(cardNumber, username).orElseThrow(() -> new AccountsNotFoundException("Account not found"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            log.warn("Attempted to close account {} with non-zero balance", cardNumber);
            throw new AccountBalanceNotZeroException("Account balance must be zero to close the account");
        }

        accountRepository.delete(account);
        log.info("Account {} closed successfully", cardNumber);
    }

    @CacheEvict(value = {"accounts", "userAccounts"}, allEntries = true)
    public void clearAllCaches() {
        log.info("All caches for accounts have been cleared.");
    }
}
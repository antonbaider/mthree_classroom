package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.AccountDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.CurrencyType;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.exception.AccountBalanceNotZeroException;
import com.mthree.bankmthree.exception.AccountsNotFoundException;
import com.mthree.bankmthree.exception.IllegalArgumentsException;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.util.CardNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CardNumberGenerator cardNumberGenerator;
    private final UserMapper userMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, CardNumberGenerator cardNumberGenerator, UserMapper userMapper) {
        this.accountRepository = accountRepository;
        this.cardNumberGenerator = cardNumberGenerator;
        this.userMapper = userMapper;
    }

    @Transactional
    public Account createAndInitializeAccount(CurrencyType currency, User user) {
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
            throw new RuntimeException("Unable to generate a unique card number after " + maxAttempts + " attempts");
        }
        Account account = new Account();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        account.setCardNumber(cardNumberGenerator.generateCardNumber());
        account.setExpirationDate(expirationDate);

        return account;
    }

    @Transactional
    public Account createAccount(User user, CurrencyType currency) {
        if (user.getAccounts().stream().anyMatch(account -> account.getCurrency() == currency)) {
            throw new IllegalArgumentsException("Account with currency " + currency + " already exists.");
        }
        Account account = createAndInitializeAccount(currency, user);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Set<AccountDTO> getUserAccounts(User user) {
        return user.getAccounts().stream().map(userMapper::toAccountDTO).collect(Collectors.toSet());
    }

    @Transactional
    public void closeAccount(String cardNumber, String username) {
        Account account = (Account) accountRepository.findByCardNumberAndUserUsername(cardNumber, username).orElseThrow(() -> new AccountsNotFoundException("Account not found"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new AccountBalanceNotZeroException("Account balance must be zero to close the account");
        }

        accountRepository.delete(account);
    }
}
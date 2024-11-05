package com.mthree.bankmthree.service;

import com.mthree.bankmthree.constants.MessageConstants;
import com.mthree.bankmthree.dto.account.AccountDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.entity.enums.CurrencyType;
import com.mthree.bankmthree.exception.account.AccountBalanceNotZeroException;
import com.mthree.bankmthree.exception.account.AccountsNotFoundException;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.util.CardNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing accounts.
 */
@Service
@Slf4j
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

    /**
     * Creates and initializes a new account for the user.
     *
     * @param currency the currency type for the account
     * @param user     the user for whom the account is being created
     * @return the initialized account
     */
    @Transactional
    protected Account createAndInitializeAccount(CurrencyType currency, User user) {
        // Logging the creation of a new account
        log.info(MessageConstants.Logs.CREATING_NEW_ACCOUNT, user.getUsername(), currency);

        String cardNumber = "";
        int maxAttempts = 5;
        boolean isUnique = false;
        LocalDate creationDate = LocalDate.now();
        LocalDate expirationDate = creationDate.plusYears(5);

        // Attempt to generate a unique card number
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            cardNumber = cardNumberGenerator.generateCardNumber();
            if (!accountRepository.existsByCardNumber(cardNumber)) {
                isUnique = true;
                break;
            }
        }

        // If unable to generate a unique card number, log and throw exception
        if (!isUnique) {
            log.error(MessageConstants.Logs.UNABLE_TO_GENERATE_CARD_NUMBER, maxAttempts);
            String exceptionMessage = String.format(MessageConstants.Exceptions.UNABLE_TO_GENERATE_CARD_NUMBER, maxAttempts);
            throw new RuntimeException(exceptionMessage);
        }

        // Initialize the account
        Account account = new Account();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        account.setCardNumber(cardNumber);
        account.setExpirationDate(expirationDate);

        // Log successful account creation
        log.info(MessageConstants.Logs.ACCOUNT_CREATED_SUCCESSFULLY, user.getUsername());

        return account;
    }

    /**
     * Creates a new account for the user with the specified currency.
     *
     * @param user     the user for whom the account is being created
     * @param currency the currency type for the account
     * @return the created account
     */
    @Transactional
    @CachePut(value = "accounts", key = "#user.username + '-' + #currency")
    public Account createAccount(User user, CurrencyType currency) {
        // Check if the user already has an account with the specified currency
        boolean accountExists = user.getAccounts().stream()
                .anyMatch(account -> account.getCurrency() == currency);

        if (accountExists) {
            String exceptionMsg = String.format(MessageConstants.Exceptions.ACCOUNT_ALREADY_EXISTS, currency);
            log.warn(exceptionMsg);
            throw new IllegalArgumentException(exceptionMsg);
        }

        // Create and initialize the account
        Account account = createAndInitializeAccount(currency, user);
        return accountRepository.save(account);
    }

    /**
     * Retrieves all accounts for the specified user.
     *
     * @param user the user whose accounts are to be retrieved
     * @return a set of AccountDTOs representing the user's accounts
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "userAccounts", key = "#user.username")
    public Set<AccountDTO> getUserAccounts(User user) {
        return user.getAccounts().stream()
                .map(userMapper::toAccountDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Closes the specified account for the user.
     *
     * @param cardNumber the card number of the account to be closed
     * @param username   the username of the user closing the account
     */
    @Transactional
    @CacheEvict(value = "userAccounts", key = "#username")
    public void closeAccount(String cardNumber, String username) {
        // Logging the initiation of account closure
        log.info(MessageConstants.Logs.CLOSING_ACCOUNT, cardNumber, username);

        // Retrieve the account; throw exception if not found
        Account account = accountRepository.findByCardNumberAndUserUsername(cardNumber, username)
                .orElseThrow(() -> new AccountsNotFoundException(MessageConstants.Exceptions.ACCOUNT_NOT_FOUND));

        // Check if the account balance is zero
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            log.warn(MessageConstants.Logs.ATTEMPTED_TO_CLOSE_NON_ZERO_BALANCE, cardNumber);
            throw new AccountBalanceNotZeroException(MessageConstants.Exceptions.ACCOUNT_BALANCE_NOT_ZERO);
        }

        // Delete the account
        accountRepository.delete(account);

        // Log successful account closure
        log.info(MessageConstants.Logs.ACCOUNT_CLOSED_SUCCESSFULLY, cardNumber);
    }

    /**
     * Clears all caches related to accounts.
     */
    @CacheEvict(value = {"accounts", "userAccounts"}, allEntries = true)
    public void clearAllCaches() {
        log.info(MessageConstants.Logs.CLEAR_ALL_CACHES);
    }
}
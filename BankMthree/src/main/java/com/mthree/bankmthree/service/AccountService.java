package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.account.AccountDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.entity.enums.CurrencyType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * AccountService interface defines the operations related to managing user accounts
 * in the banking application. It provides methods for creating, retrieving, and
 * closing accounts.
 */
public interface AccountService {

    /**
     * Creates and initializes a new Account for the specified user and currency.
     * This method generates a unique card number and sets the initial balance.
     *
     * @param currency The currency type for the new account.
     * @param user     The User entity for whom the account is being created.
     * @return The created Account entity.
     */
    @Transactional
    Account createAndInitializeAccount(CurrencyType currency, User user);

    /**
     * Creates a new account for the specified user and currency, ensuring no duplicate
     * accounts exist.
     *
     * @param user     The User entity for whom the account is being created.
     * @param currency The currency type for the new account.
     * @return The created Account entity.
     */
    @Transactional
    Account createAccount(User user, CurrencyType currency);

    /**
     * Retrieves all accounts associated with the specified user.
     *
     * @param user The User entity whose accounts are to be retrieved.
     * @return A set of AccountDTOs representing the user's accounts.
     */
    @Transactional(readOnly = true)
    Set<AccountDTO> getUserAccounts(User user);

    /**
     * Closes the specified account for the user, ensuring the balance is zero before closure.
     *
     * @param cardNumber The card number of the account to be closed.
     * @param username   The username of the user attempting to close the account.
     */
    @Transactional
    void closeAccount(String cardNumber, String username);
}
package com.mthree.bankmthree.service;

import com.mthree.bankmthree.constants.MessageConstants;
import com.mthree.bankmthree.dto.transaction.TransactionResponse;
import com.mthree.bankmthree.dto.transaction.TransferRequest;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.Transaction;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.entity.enums.Role;
import com.mthree.bankmthree.exception.account.AccountsNotFoundException;
import com.mthree.bankmthree.exception.account.ReceiverAccountNotFoundException;
import com.mthree.bankmthree.exception.transaction.UnauthorizedTransferException;
import com.mthree.bankmthree.mapper.TransactionMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.repository.TransactionRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing transactions.
 */
@Slf4j
@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, UserService userService, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Transfers money between two accounts using card numbers.
     *
     * @param transferRequest the transfer request containing sender and receiver card numbers and amount
     * @param username        the username initiating the transfer
     * @return the created transaction
     */
    @Transactional
    public Transaction transferMoneyUsingCardNumbers(@Valid TransferRequest transferRequest, String username) {
        // Logging the start of the transfer
        log.info(MessageConstants.Logs.TRANSFER_STARTED, maskCardNumber(transferRequest.getSenderCardNumber()), maskCardNumber(transferRequest.getReceiverCardNumber()));

        // Validating card number formats
        validateCardNumberFormat(transferRequest.getSenderCardNumber());
        validateCardNumberFormat(transferRequest.getReceiverCardNumber());

        // Validating same-account transfer
        validateTransferRequest(transferRequest);

        // Fetching sender and receiver accounts
        Account sender = accountRepository.findByCardNumber(transferRequest.getSenderCardNumber()).orElseThrow(() -> new AccountsNotFoundException(MessageConstants.Exceptions.SENDER_ACCOUNT_NOT_FOUND));
        Account receiver = accountRepository.findByCardNumber(transferRequest.getReceiverCardNumber()).orElseThrow(() -> new ReceiverAccountNotFoundException(MessageConstants.Exceptions.RECEIVER_ACCOUNT_NOT_FOUND));

        // Validating the transfer details
        validateTransfer(sender, receiver, transferRequest.getAmount(), username);

        // Performing the transfer
        sender.setBalance(sender.getBalance().subtract(transferRequest.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transferRequest.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Creating the transaction record
        Transaction transaction = new Transaction();
        transaction.setAmount(transferRequest.getAmount());
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        // Logging the successful transfer
        log.info(MessageConstants.Logs.TRANSFER_COMPLETED, maskCardNumber(transferRequest.getSenderCardNumber()), maskCardNumber(transferRequest.getReceiverCardNumber()));

        return transactionRepository.save(transaction);
    }

    /**
     * Transfers money between two users.
     *
     * @param senderUserId   the sender's user ID
     * @param receiverUserId the receiver's user ID
     * @param amount         the amount to transfer
     * @param username       the username initiating the transfer
     * @return the created transaction
     */
    @Transactional
    public Transaction transferMoneyBetweenUsers(@Valid Long senderUserId, @Valid Long receiverUserId, @Valid @Positive BigDecimal amount, @NotBlank String username) {
        // Fetching sender and receiver accounts by user IDs
        Account sender = accountRepository.findById(senderUserId).orElseThrow(() -> new AccountsNotFoundException(MessageConstants.Exceptions.SENDER_ACCOUNT_NOT_FOUND));

        Account receiver = accountRepository.findById(receiverUserId).orElseThrow(() -> new ReceiverAccountNotFoundException(MessageConstants.Exceptions.RECEIVER_ACCOUNT_NOT_FOUND));

        // **New Validation: Prevent transferring to the same account**
        UserTransferRequestValidation(sender, receiver);

        // Validating the transfer details
        validateTransfer(sender, receiver, amount, username);

        // Performing the transfer
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Creating the transaction record
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setSender(sender.getUser());
        transaction.setReceiver(receiver.getUser());
        transaction.setTimestamp(LocalDateTime.now());

        // Logging the successful transfer between users
        log.info(MessageConstants.Logs.TRANSFER_BETWEEN_USERS_COMPLETED, amount, sender.getUser().getUsername(), receiver.getUser().getUsername());

        return transactionRepository.save(transaction);
    }

    // Validation of transfer request
    private void UserTransferRequestValidation(Account sender, Account receiver) {
        if (sender.getId().equals(receiver.getId())) {
            log.warn(MessageConstants.Logs.SAME_ACCOUNT_TRANSFER_LOG, maskCardNumber(sender.getCardNumber()));
            throw new IllegalArgumentException(MessageConstants.Exceptions.SAME_ACCOUNT_TRANSFER);
        }
    }

    /**
     * Retrieves the transaction history for a user.
     *
     * @param userId the user ID
     * @return a list of TransactionResponse objects representing the transaction history
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "transactionHistory", key = "#userId")
    public List<TransactionResponse> getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);
        return transactions.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Clears the transaction history cache for a specific user.
     *
     * @param userId the user ID whose cache is to be cleared
     */
    @CacheEvict(value = "transactionHistory", key = "#userId")
    public void clearUserCache(Long userId) {
        log.info(MessageConstants.Logs.CLEARING_CACHE_FOR_USER, userId);
    }

    /**
     * Validates the transfer request by checking card number formats and preventing same-account transfers.
     *
     * @param transferRequest the transfer request containing sender and receiver card numbers
     */
    private void validateTransferRequest(TransferRequest transferRequest) {
        // Prevent transferring to the same account
        if (transferRequest.getSenderCardNumber().equals(transferRequest.getReceiverCardNumber())) {
            log.warn(MessageConstants.Logs.SAME_ACCOUNT_TRANSFER_LOG, maskCardNumber(transferRequest.getSenderCardNumber()));
            throw new IllegalArgumentException(MessageConstants.Exceptions.SAME_ACCOUNT_TRANSFER);
        }
    }

    /**
     * Validates the transfer details to ensure compliance with business rules.
     *
     * @param sender   the sender's account
     * @param receiver the receiver's account
     * @param amount   the amount to transfer
     * @param username the username initiating the transfer
     */
    private void validateTransfer(Account sender, Account receiver, BigDecimal amount, String username) {
        // Logging the validation process
        log.info(MessageConstants.Logs.VALIDATING_TRANSFER, username);

        User user = userService.findByUsername(username);
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);

        // Check for authorization
        if (!isAdmin && !sender.getUser().getUsername().equals(username)) {
            log.warn(MessageConstants.Logs.UNAUTHORIZED_TRANSFER, username);
            throw new UnauthorizedTransferException(MessageConstants.Exceptions.UNAUTHORIZED_TRANSFER);
        }

        // Check for currency mismatch
        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            log.warn(MessageConstants.Logs.CURRENCY_MISMATCH_LOG);
            throw new IllegalArgumentException(MessageConstants.Exceptions.CURRENCY_MISMATCH);
        }

        // Check for valid transfer amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn(MessageConstants.Logs.INVALID_TRANSFER_AMOUNT, amount);
            throw new IllegalArgumentException(MessageConstants.Exceptions.INVALID_TRANSFER_AMOUNT);
        }

        // Check for sufficient funds
        if (!isAdmin && sender.getBalance().compareTo(amount) < 0) {
            log.warn(MessageConstants.Logs.INSUFFICIENT_FUNDS, username, maskCardNumber(sender.getCardNumber()));
            throw new IllegalArgumentException(MessageConstants.Exceptions.INSUFFICIENT_BALANCE);
        }
    }

    /**
     * Validates the format of the card number.
     *
     * @param cardNumber the card number to validate
     */
    private void validateCardNumberFormat(String cardNumber) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) { // Example: 16-digit card number
            log.warn(MessageConstants.Logs.INVALID_CARD_NUMBER_FORMAT_LOG, cardNumber);
            throw new IllegalArgumentException(MessageConstants.Exceptions.INVALID_CARD_NUMBER_FORMAT);
        }
    }

    /**
     * Masks the card number for security purposes.
     *
     * @param cardNumber the original card number
     * @return the masked card number
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.transaction.TransactionResponse;
import com.mthree.bankmthree.dto.transaction.TransferRequest;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.enums.Role;
import com.mthree.bankmthree.entity.Transaction;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.exception.account.AccountsNotFoundException;
import com.mthree.bankmthree.exception.transaction.UnauthorizedTransferException;
import com.mthree.bankmthree.mapper.TransactionMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.repository.TransactionRepository;
import jakarta.validation.Valid;
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

@Slf4j
@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public Transaction transferMoneyByCardNumber(@Valid TransferRequest transferRequest, String username) {
        log.info("Starting transfer from {} to {}", transferRequest.getSenderCardNumber(), transferRequest.getReceiverCardNumber());
        Account sender = accountRepository.findByCardNumber(transferRequest.getSenderCardNumber()).orElseThrow(() -> new AccountsNotFoundException("Sender account not found"));
        Account receiver = accountRepository.findByCardNumber(transferRequest.getReceiverCardNumber()).orElseThrow(() -> new AccountsNotFoundException("Receiver account not found"));

        validateTransfer(sender, receiver, transferRequest.getAmount(), username);

        sender.setBalance(sender.getBalance().subtract(transferRequest.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transferRequest.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setAmount(transferRequest.getAmount());
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        log.info("Transfer from {} to {} completed successfully", transferRequest.getSenderCardNumber(), transferRequest.getReceiverCardNumber());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transferMoney(Long senderUserId, Long receiverUserId, BigDecimal amount, String username) {
        Account sender = accountRepository.findById(senderUserId).orElseThrow(() -> new AccountsNotFoundException("Sender account not found"));
        Account receiver = accountRepository.findById(receiverUserId).orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        validateTransfer(sender, receiver, amount, username);

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setSender(sender.getUser());
        transaction.setReceiver(receiver.getUser());
        return transactionRepository.save(transaction);
    }

    @Cacheable(value = "transactionHistory", key = "#userId")
    public List<TransactionResponse> getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);

        return transactions.stream().map(TransactionMapper.INSTANCE::toResponse).collect(Collectors.toList());
    }

    @CacheEvict(value = {"transactions", "transactionHistory"}, allEntries = true)
    public void clearCache() {
        log.info("Clearing all cache entries for transactions and transaction history.");
    }

    private void validateTransfer(Account sender, Account receiver, BigDecimal amount, String username) {
        log.info("Validating transfer for user {}", username);
        User user = userService.findByUsername(username);
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);

        if (!isAdmin && !sender.getUser().getUsername().equals(username)) {
            log.warn("Unauthorized transfer attempt by user {}", username);
            throw new UnauthorizedTransferException("You do not own the sender account");
        }

        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            log.warn("Currency mismatch between sender and receiver accounts");
            throw new IllegalArgumentException("Currency mismatch between accounts");
        }

        if (!isAdmin && sender.getBalance().compareTo(amount) < 0) {
            log.warn("Insufficient funds for user {} on account {}", username, sender.getCardNumber());
            throw new IllegalArgumentException("Insufficient balance in sender's account");
        }
    }
}
package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.TransactionResponse;
import com.mthree.bankmthree.dto.TransferRequest;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.Role;
import com.mthree.bankmthree.entity.Transaction;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.exception.AccountsNotFoundException;
import com.mthree.bankmthree.exception.IllegalArgumentsException;
import com.mthree.bankmthree.exception.UnauthorizedTransferException;
import com.mthree.bankmthree.mapper.TransactionMapper;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.repository.TransactionRepository;
import com.mthree.bankmthree.repository.UserRepository;
import com.mthree.bankmthree.util.CardNumberGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardNumberGenerator cardNumberGenerator;
    private final UserService userService;


    @Autowired
    public TransactionService(UserMapper userMapper, UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, PasswordEncoder passwordEncoder, CardNumberGenerator cardNumberGenerator, UserService userService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardNumberGenerator = cardNumberGenerator;
        this.userService = userService;
    }

    @Transactional
    public Transaction transferMoneyByCardNumber(@Valid TransferRequest transferRequest, String username) {
        Account sender = accountRepository.findByCardNumber(transferRequest.getSenderCardNumber()).orElseThrow(() -> new AccountsNotFoundException("Sender account not found"));

        Account receiver = accountRepository.findByCardNumber(transferRequest.getReceiverCardNumber()).orElseThrow(() -> new AccountsNotFoundException("Receiver account not found"));

        validateTransfer(sender, receiver, transferRequest.getAmount(), username);

        Transaction transaction = new Transaction();
        sender.setBalance(sender.getBalance().subtract(transferRequest.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transferRequest.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        transaction.setAmount(transferRequest.getAmount());
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setTimestamp(LocalDateTime.now());
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

    public List<TransactionResponse> getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);

        return transactions.stream()
                .map(TransactionMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    private void validateTransfer(Account sender, Account receiver, BigDecimal amount, String username) {
        User user = userService.findByUsername(username);
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);

        if (!isAdmin && !sender.getUser().getUsername().equals(username)) {
            throw new UnauthorizedTransferException("You do not own the sender account");
        }

        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            throw new IllegalArgumentsException("Currency mismatch between accounts");
        }

        if (!isAdmin && sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentsException("Insufficient balance in sender's account");
        }
    }
}
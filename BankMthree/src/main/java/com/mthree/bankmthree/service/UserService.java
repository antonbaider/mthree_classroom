package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.AccountDTO;
import com.mthree.bankmthree.dto.RegisterRequest;
import com.mthree.bankmthree.dto.TransferRequestDTO;
import com.mthree.bankmthree.dto.UserDTO;
import com.mthree.bankmthree.entity.*;
import com.mthree.bankmthree.exception.*;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.AccountRepository;
import com.mthree.bankmthree.repository.TransactionRepository;
import com.mthree.bankmthree.repository.UserRepository;
import com.mthree.bankmthree.util.CardNumberGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.IllegalArgumentException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardNumberGenerator cardNumberGenerator;


    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, PasswordEncoder passwordEncoder, CardNumberGenerator cardNumberGenerator) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    public UserDTO getUserDto(User user) {
        return userMapper.toUserDTO(user);
    }

    public User getUser(RegisterRequest userDTO) {
        return userMapper.toUser(userDTO);
    }

    @Transactional
    public UserDTO createUser(@Valid RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if (userRepository.existsBySsn(registerRequest.getSsn())) {
            throw new SsnAlreadyExistsException("SSN already exists");
        }
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number already exists");
        }

        User user = userMapper.toUser(registerRequest);
        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);
        user.setType(UserType.STANDARD);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Account usdAccount = createAndInitializeAccount(CurrencyType.USD, user);
        Account eurAccount = createAndInitializeAccount(CurrencyType.EUR, user);

        user.setAccounts(new HashSet<>(Arrays.asList(usdAccount, eurAccount)));

        try {
            User savedUser = userRepository.save(user);

            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    private Account createAndInitializeAccount(CurrencyType currency, User user) {
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
            throw new IllegalArgumentException("Account with currency " + currency + " already exists.");
        }
        Account account = createAndInitializeAccount(currency, user);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Set<AccountDTO> getUserAccounts(User user) {
        return user.getAccounts().stream().map(account -> userMapper.toAccountDTO(account)).collect(Collectors.toSet());
    }



    public void addFamilyMember(Long userId, Long familyMemberId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User familyMember = userRepository.findById(familyMemberId).orElseThrow(() -> new UsernameNotFoundException("Family member not found"));
        user.getFamily().add(familyMember);
        userRepository.save(user);
    }

    public Set<User> getFamilyMembers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getFamily();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public Set<UserDTO> convertUsersToDTOs(Set<User> users) {
        return users.stream().map(userMapper::toUserDTO).collect(Collectors.toSet());
    }

}
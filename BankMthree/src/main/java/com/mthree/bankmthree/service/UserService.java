package com.mthree.bankmthree.service;

import com.mthree.bankmthree.dto.RegisterRequest;
import com.mthree.bankmthree.dto.UpdateUserRequest;
import com.mthree.bankmthree.dto.UserDTO;
import com.mthree.bankmthree.entity.*;
import com.mthree.bankmthree.exception.PhoneAlreadyExistsException;
import com.mthree.bankmthree.exception.SsnAlreadyExistsException;
import com.mthree.bankmthree.exception.UserAlreadyExistsException;
import com.mthree.bankmthree.exception.UserNotFoundException;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, AccountService accountService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @Transactional(readOnly = true)
    public UserDTO getUserDto(User user) {
        return userMapper.toUserDTO(user);
    }

    public User getUser(RegisterRequest userDTO) {
        return userMapper.toUser(userDTO);
    }

    @Transactional
    public UserDTO createUser(@Valid RegisterRequest registerRequest) {
        validateUserFields(registerRequest);

        User user = userMapper.toUser(registerRequest);
        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);
        user.setType(UserType.STANDARD);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Account usdAccount = accountService.createAndInitializeAccount(CurrencyType.USD, user);
        Account eurAccount = accountService.createAndInitializeAccount(CurrencyType.EUR, user);

        user.setAccounts(new HashSet<>(Arrays.asList(usdAccount, eurAccount)));

        try {
            User savedUser = userRepository.save(user);
            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public UserDTO updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        validateUser(updateUserRequest, user);

        User updatedUser = userRepository.save(user);
        return getUserDto(updatedUser);
    }

    private void validateUser(UpdateUserRequest updateUserRequest, User user) {
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getPhone() != null) {
            user.setPhone(updateUserRequest.getPhone());
        }
        if (updateUserRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
    }

    private void validateUserFields(@Valid RegisterRequest registerRequest) {
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
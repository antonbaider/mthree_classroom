package com.mthree.bankmthree.service;

import com.mthree.bankmthree.constants.MessageConstants;
import com.mthree.bankmthree.dto.auth.RegisterRequest;
import com.mthree.bankmthree.dto.user.UpdateUserRequest;
import com.mthree.bankmthree.dto.user.UserDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.entity.enums.CurrencyType;
import com.mthree.bankmthree.entity.enums.Role;
import com.mthree.bankmthree.entity.enums.Status;
import com.mthree.bankmthree.entity.enums.UserType;
import com.mthree.bankmthree.exception.user.UserAlreadyExistsException;
import com.mthree.bankmthree.exception.user.UserNotFoundException;
import com.mthree.bankmthree.exception.user.UserPhoneAlreadyExistsException;
import com.mthree.bankmthree.exception.user.UserSsnAlreadyExistsException;
import com.mthree.bankmthree.mapper.UserMapper;
import com.mthree.bankmthree.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Slf4j
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

    /**
     * Retrieves the UserDTO for a given user.
     *
     * @param user the user entity
     * @return the UserDTO representation of the user
     */
    @Transactional(readOnly = true)
    @CacheEvict(value = "users", key = "#user.username")
    public UserDTO getUserDto(User user) {
        log.info(MessageConstants.Logs.FETCHING_USER_DTO, user.getUsername());
        return userMapper.toUserDTO(user);
    }

    /**
     * Converts a RegisterRequest DTO to a User entity.
     *
     * @param userDTO the RegisterRequest DTO
     * @return the User entity
     */
    public User getUser(RegisterRequest userDTO) {
        return userMapper.toUser(userDTO);
    }

    /**
     * Creates a new user based on the provided registration request.
     *
     * @param registerRequest the registration request containing user details
     * @return the created UserDTO
     */
    @Transactional
    @CacheEvict(value = "users", key = "#registerRequest.username")
    public UserDTO createUser(@Valid RegisterRequest registerRequest) {
        log.info(MessageConstants.Logs.CREATING_USER, registerRequest.getUsername());
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
        } catch (DataIntegrityViolationException e) {
            log.error(MessageConstants.Logs.FAILED_TO_SAVE_USER, e.getMessage());
            // Determine which field caused the violation for a more precise exception
            if (e.getMessage().contains("username")) {
                throw new UserAlreadyExistsException(MessageConstants.Exceptions.USER_ALREADY_EXISTS);
            } else if (e.getMessage().contains("email")) {
                throw new UserAlreadyExistsException(MessageConstants.Exceptions.USER_EMAIL_EXISTS);
            } else if (e.getMessage().contains("ssn")) {
                throw new UserSsnAlreadyExistsException(MessageConstants.Exceptions.USER_SSN_EXISTS);
            } else if (e.getMessage().contains("phone")) {
                throw new UserPhoneAlreadyExistsException(MessageConstants.Exceptions.USER_PHONE_EXISTS);
            } else {
                throw new UserAlreadyExistsException("A user with the provided details already exists.");
            }
        } catch (Exception e) {
            log.error(MessageConstants.Logs.FAILED_TO_SAVE_USER, e.getMessage());
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing user's details.
     *
     * @param username          the username of the user to update
     * @param updateUserRequest the request containing updated user details
     * @return the updated UserDTO
     */
    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public UserDTO updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(MessageConstants.Exceptions.USER_NOT_FOUND));

        validateAndUpdateUserFields(updateUserRequest, user);

        try {
            User updatedUser = userRepository.save(user);
            log.info(MessageConstants.Logs.USER_UPDATED_SUCCESSFULLY, username);
            return getUserDto(updatedUser);
        } catch (DataIntegrityViolationException e) {
            log.error(MessageConstants.Logs.FAILED_TO_SAVE_USER, e.getMessage());
            // Determine which field caused the violation for a more precise exception
            if (e.getMessage().contains("email")) {
                throw new UserAlreadyExistsException(MessageConstants.Exceptions.USER_EMAIL_EXISTS);
            } else if (e.getMessage().contains("phone")) {
                throw new UserPhoneAlreadyExistsException(MessageConstants.Exceptions.USER_PHONE_EXISTS);
            } else {
                throw new UserAlreadyExistsException("A user with this email or phone number already exists.");
            }
        }
    }

    /**
     * Validates and updates user fields based on the update request.
     *
     * @param updateUserRequest the update request containing new user details
     * @param user              the existing user entity to update
     */
    private void validateAndUpdateUserFields(UpdateUserRequest updateUserRequest, User user) {
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }

        if (updateUserRequest.getEmail() != null && !user.getEmail().equals(updateUserRequest.getEmail())) {
            if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new UserAlreadyExistsException(MessageConstants.Exceptions.USER_EMAIL_EXISTS);
            }
            user.setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getPhone() != null && !user.getPhone().equals(updateUserRequest.getPhone())) {
            if (userRepository.existsByPhone(updateUserRequest.getPhone())) {
                throw new UserPhoneAlreadyExistsException(MessageConstants.Exceptions.USER_PHONE_EXISTS);
            }
            user.setPhone(updateUserRequest.getPhone());
        }

        if (updateUserRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
    }

    /**
     * Validates user fields during registration to ensure uniqueness.
     *
     * @param registerRequest the registration request containing user details
     */
    private void validateUserFields(@Valid RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException(MessageConstants.Exceptions.USER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(MessageConstants.Exceptions.USER_EMAIL_EXISTS);
        }
        if (userRepository.existsBySsn(registerRequest.getSsn())) {
            throw new UserSsnAlreadyExistsException(MessageConstants.Exceptions.USER_SSN_EXISTS);
        }
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new UserPhoneAlreadyExistsException(MessageConstants.Exceptions.USER_PHONE_EXISTS);
        }
    }

    /**
     * Adds a family member to a user's family set.
     *
     * @param userId         the ID of the user
     * @param familyMemberId the ID of the family member to add
     */
    public void addFamilyMember(Long userId, Long familyMemberId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageConstants.Exceptions.USER_NOT_FOUND));
        User familyMember = userRepository.findById(familyMemberId).orElseThrow(() -> new UserNotFoundException(MessageConstants.Exceptions.FAMILY_MEMBER_NOT_FOUND));

        user.getFamily().add(familyMember);
        userRepository.save(user);
        log.info(MessageConstants.Logs.ADDING_FAMILY_MEMBER, familyMemberId, userId);
    }

    /**
     * Retrieves the family members of a user.
     *
     * @param userId the ID of the user
     * @return a set of family members
     */
    public Set<User> getFamilyMembers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageConstants.Exceptions.USER_NOT_FOUND));
        log.info(MessageConstants.Logs.FETCHING_FAMILY_MEMBERS, userId);
        return user.getFamily();
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the found User entity
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public User findByUsername(String username) {
        log.info(MessageConstants.Logs.FINDING_USER_BY_USERNAME, username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(MessageConstants.Exceptions.USER_NOT_FOUND));
    }

    /**
     * Converts a set of User entities to a set of UserDTOs.
     *
     * @param users the set of User entities
     * @return the set of UserDTOs
     */
    public Set<UserDTO> convertUsersToDTOs(Set<User> users) {
        log.info(MessageConstants.Logs.CONVERTING_USERS_TO_DTO);
        return users.stream().map(userMapper::toUserDTO).collect(Collectors.toSet());
    }
}
package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.account.AccountDTO;
import com.mthree.bankmthree.dto.account.CloseAccountRequest;
import com.mthree.bankmthree.service.AccountService;
import com.mthree.bankmthree.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Controller class responsible for handling HTTP requests related to user bank accounts.
 * It provides endpoints for creating accounts, retrieving user accounts, and closing accounts.
 * This controller leverages Spring Security for authorization and uses Swagger annotations for API documentation.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Account Controller", description = "Endpoints for user bank accounts")
public class AccountController {

    /**
     * Service responsible for account-related business logic.
     */
    private final AccountService accountService;

    /**
     * Service responsible for user-related business logic.
     */
    private final UserService userService;

    /**
     * Constructor for AccountController.
     * Uses constructor-based dependency injection to inject AccountService and UserService.
     *
     * @param userService    Service for managing users.
     * @param accountService Service for managing accounts.
     */
    @Autowired
    public AccountController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * Endpoint to create a new bank account for the authenticated user in a specified currency.
     *
     * @param accountDTO  The data transfer object containing account creation details, such as currency.
     * @param userDetails The details of the currently authenticated user, automatically injected by Spring Security.
     * @return A ResponseEntity containing a success message upon successful account creation.
     * @operation Creates a new bank account for the user in the specified currency.
     * The user must be authenticated and have either 'USER' or 'ADMIN' role.
     */
    @Operation(summary = "Create an account in a specific currency")
    @PostMapping("/accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountDTO accountDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the User entity based on the authenticated user's username
        String username = userDetails.getUsername();
        // Find the User entity; assumes userService.findByUsername throws an exception if not found
        var user = userService.findByUsername(username);
        // Create the account using the AccountService
        accountService.createAccount(user, accountDTO.getCurrency());
        // Return a success response
        return ResponseEntity.ok("Account created successfully");
    }

    /**
     * Endpoint to retrieve all bank accounts associated with the authenticated user.
     *
     * @param userDetails The details of the currently authenticated user, automatically injected by Spring Security.
     * @return A ResponseEntity containing a set of AccountDTOs representing the user's accounts.
     * @operation Retrieves all bank accounts for the authenticated user.
     * The user must be authenticated and have either 'USER' or 'ADMIN' role.
     */
    @Operation(summary = "Get all accounts of the current user")
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<AccountDTO>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the User entity based on the authenticated user's username
        String username = userDetails.getUsername();
        var user = userService.findByUsername(username);
        // Fetch all accounts associated with the user using AccountService
        Set<AccountDTO> accountDTOs = accountService.getUserAccounts(user);
        // Return the set of AccountDTOs in the response
        return ResponseEntity.ok(accountDTOs);
    }

    /**
     * Endpoint to close a bank account for the authenticated user, provided the account balance is zero.
     *
     * @param request     The request payload containing the card number of the account to be closed.
     * @param userDetails The details of the currently authenticated user, automatically injected by Spring Security.
     * @return A ResponseEntity containing a success message upon successful account closure.
     * @operation Closes a bank account for the authenticated user if the account balance is zero.
     * The user must be authenticated and have either 'USER' or 'ADMIN' role.
     */
    @Operation(summary = "Close an account if the balance is zero")
    @DeleteMapping("/accounts/close")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> closeAccount(@RequestBody CloseAccountRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the authenticated user's username
        String username = userDetails.getUsername();
        // Close the specified account using the AccountService
        accountService.closeAccount(request.getCardNumber(), username);
        // Return a success response
        return ResponseEntity.ok("Account closed successfully");
    }
}
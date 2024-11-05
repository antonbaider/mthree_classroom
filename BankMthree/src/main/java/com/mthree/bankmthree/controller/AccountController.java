package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.AccountDTO;
import com.mthree.bankmthree.dto.CloseAccountRequest;
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

@RestController
@RequestMapping("/api")
@Tag(name = "Account Controller", description = "Endpoints for user bank accounts")
public class AccountController {
    private final AccountService accountService;
    UserService userService;

    @Autowired
    public AccountController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Operation(summary = "Create an account in a specific currency")
    @PostMapping("/accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountDTO accountDTO, @AuthenticationPrincipal UserDetails userDetails) {
        accountService.createAccount(userService.findByUsername(userDetails.getUsername()), accountDTO.getCurrency());
        return ResponseEntity.ok("Account created successfully");
    }

    @Operation(summary = "Get all accounts of the current user")
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<AccountDTO>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        Set<AccountDTO> accountDTOs = accountService.getUserAccounts(userService.findByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(accountDTOs);
    }

    @Operation(summary = "Close an account if the balance is zero")
    @DeleteMapping("/accounts/close")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> closeAccount(@RequestBody CloseAccountRequest request,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        accountService.closeAccount(request.getCardNumber(), userDetails.getUsername());
        return ResponseEntity.ok("Account closed successfully");
    }
}
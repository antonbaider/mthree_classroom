package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.AccountDTO;
import com.mthree.bankmthree.dto.TransferRequestDTO;
import com.mthree.bankmthree.dto.UserDTO;
import com.mthree.bankmthree.entity.CurrencyType;
import com.mthree.bankmthree.entity.User;
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

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Operations related to users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @Operation(summary = "Register a new user")
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
//        UserDTO newUser = userService.createUser(userDTO);
//        UserDTO newUserDTO = userService.getUserDto(newUser);
//        return ResponseEntity.ok(newUserDTO);
//    }

    @Operation(summary = "Get the current user's profile")
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        UserDTO userDTO = userService.getUserDto(user);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Create an account in a specific currency")
    @PostMapping("/accounts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createAccount(@RequestParam CurrencyType currency, @AuthenticationPrincipal UserDetails userDetails) {
        userService.createAccount(userService.findByUsername(userDetails.getUsername()), currency);
        return ResponseEntity.ok("Account created successfully");
    }

    @Operation(summary = "Get all accounts of the current user")
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<AccountDTO>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        Set<AccountDTO> accountDTOs = userService.getUserAccounts(userService.findByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(accountDTOs);
    }

    @Operation(summary = "Transfer money between accounts")
    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> transferMoney(@RequestParam Long senderAccountId, @RequestParam Long receiverAccountId, @RequestParam BigDecimal amount) {
        userService.transferMoney(senderAccountId, receiverAccountId, amount);
        return ResponseEntity.ok("Successfully sent");
    }

    @Operation(summary = "Transfer money between accounts using card numbers")
    @PostMapping("/transferByCard")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> transferMoneyByCard(
            @Valid @RequestBody TransferRequestDTO transferRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.transferMoneyByCardNumber(transferRequest, userDetails.getUsername());
        return ResponseEntity.ok("Transfer successful");
    }
}
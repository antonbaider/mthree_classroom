package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.AccountDTO;
import com.mthree.bankmthree.dto.UserDTO;
import com.mthree.bankmthree.entity.CurrencyType;
import com.mthree.bankmthree.entity.User;
import com.mthree.bankmthree.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
}
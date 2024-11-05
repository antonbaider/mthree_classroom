package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.TransactionResponse;
import com.mthree.bankmthree.dto.TransferRequest;
import com.mthree.bankmthree.dto.TransferRequestByUserId;
import com.mthree.bankmthree.entity.Transaction;
import com.mthree.bankmthree.mapper.TransactionMapper;
import com.mthree.bankmthree.service.TransactionService;
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

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions Controller", description = "Endpoints for user transactions")
public class TransactionController {
    UserService userService;
    TransactionService transactionService;

    @Autowired
    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Transfer money between users by account_id")
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionResponse> transferMoney(@Valid @RequestBody TransferRequestByUserId transferRequest, @AuthenticationPrincipal UserDetails userDetails) {
        TransactionResponse response = getTransactionResponse(transferRequest, userDetails);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Transfer money between accounts using account numbers")
    @PostMapping("/transferByCard")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionResponse> transferMoneyByCard(@Valid @RequestBody TransferRequest transferRequest, @AuthenticationPrincipal UserDetails userDetails) {
        TransactionResponse response = getTransactionResponse(transferRequest, userDetails);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction history for the authenticated user")
    @GetMapping("/history")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findByUsername(userDetails.getUsername()).getId();
        List<TransactionResponse> history = transactionService.getTransactionHistory(userId);
        return ResponseEntity.ok(history);
    }

    private TransactionResponse getTransactionResponse(TransferRequestByUserId transferRequest, UserDetails userDetails) {
        Transaction transaction = transactionService.transferMoney(transferRequest.getSenderAccountId(), transferRequest.getReceiverAccountId(), transferRequest.getAmount(), userDetails.getUsername());

        TransactionResponse response = TransactionMapper.INSTANCE.toResponse(transaction);
        return response;
    }

    private TransactionResponse getTransactionResponse(TransferRequest transferRequest, UserDetails userDetails) {
        Transaction transaction = transactionService.transferMoneyByCardNumber(transferRequest, userDetails.getUsername());

        TransactionResponse response = TransactionMapper.INSTANCE.toResponse(transaction);
        return response;
    }
}
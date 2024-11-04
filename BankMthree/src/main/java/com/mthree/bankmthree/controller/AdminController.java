package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.TransferRequestByUserId;
import com.mthree.bankmthree.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    TransactionService transactionService;

    @Autowired
    public AdminController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Transfer money between users by user_id")
    @PostMapping("/adminTransfer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminTransferMoney(@Valid @RequestBody TransferRequestByUserId transferRequest, @AuthenticationPrincipal UserDetails userDetails) {
        transactionService.transferMoney(transferRequest.getSenderAccountId(), transferRequest.getReceiverAccountId(), transferRequest.getAmount(), userDetails.getUsername());
        return ResponseEntity.ok("Successfully sent");
    }
}

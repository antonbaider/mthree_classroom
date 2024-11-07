package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.transaction.TransactionResponse;
import com.mthree.bankmthree.dto.transaction.TransferRequestByUserId;
import com.mthree.bankmthree.entity.Transaction;
import com.mthree.bankmthree.mapper.TransactionMapper;
import com.mthree.bankmthree.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Controller class for handling administrative operations.
 * This includes transferring money between users with admin privileges.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "Endpoints for administrator")
public class AdminController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public AdminController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Endpoint for transferring money between users by user_id.
     * Only accessible to users with the 'ADMIN' role.
     *
     * @param transferRequest the transfer request containing sender and receiver account IDs and amount
     * @param userDetails     the details of the authenticated user
     * @return ResponseEntity containing a success message
     */
    @Operation(summary = "Transfer money between users by user_id")
    @PostMapping("/adminTransfer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> adminTransferMoney(
            @Valid @RequestBody TransferRequestByUserId transferRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Perform the money transfer
        transactionService.transferMoneyBetweenUsers(
                transferRequest.getSenderAccountId(),
                transferRequest.getReceiverAccountId(),
                transferRequest.getAmount(),
                userDetails.getUsername());
        TransactionResponse response = getTransactionResponse(transferRequest, userDetails);

        // Return a successful response
        return ResponseEntity.ok(new ApiResponse("Successfully sent", response));
    }

    private TransactionResponse getTransactionResponse(TransferRequestByUserId transferRequest, UserDetails userDetails) {
        Transaction transaction = transactionService.transferMoneyBetweenUsers(
                transferRequest.getSenderAccountId(),
                transferRequest.getReceiverAccountId(),
                transferRequest.getAmount(),
                userDetails.getUsername());

        return transactionMapper.toResponse(transaction);
    }
}
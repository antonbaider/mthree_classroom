package com.mthree.bankmthree.repository;

import com.mthree.bankmthree.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderIdOrReceiverIdOrderByTimestampDesc(Long userId, Long userId1);
}
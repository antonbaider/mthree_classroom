package com.mthree.bankmthree.repository;

import com.mthree.bankmthree.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
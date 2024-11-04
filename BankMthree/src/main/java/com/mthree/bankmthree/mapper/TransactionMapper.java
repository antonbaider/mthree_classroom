package com.mthree.bankmthree.mapper;

import com.mthree.bankmthree.dto.TransactionResponse;
import com.mthree.bankmthree.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "senderAccount.id", target = "senderAccountId")
    @Mapping(source = "receiverAccount.id", target = "receiverAccountId")
    @Mapping(expression = "java(transaction.getSenderAccount() != null ? transaction.getSenderAccount().getCardNumber() : null)", target = "senderCardNumber")
    @Mapping(expression = "java(transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getCardNumber() : null)", target = "receiverCardNumber")
    @Mapping(source = "amount", target = "amount")
    @Mapping(expression = "java(transaction.getSenderAccount().getCurrency().toString())", target = "currency")
    @Mapping(expression = "java(transaction.getSenderAccount().getBalance())", target = "balanceAfter")
    @Mapping(source = "timestamp", target = "timestamp")
    TransactionResponse toResponse(Transaction transaction);
}
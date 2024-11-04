package com.mthree.bankmthree.dto;

import com.mthree.bankmthree.entity.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private String cardNumber;
    private CurrencyType currency;
    private BigDecimal balance;
    private LocalDate expirationDate;
    ;
}

package com.mthree.bankmthree.dto;
import com.mthree.bankmthree.entity.Role;
import com.mthree.bankmthree.entity.Status;
import com.mthree.bankmthree.entity.UserType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private int id;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private Status status;
    private UserType type;
    private BigDecimal balance;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}

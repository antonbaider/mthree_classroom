package com.mthree.bankmthree.mapper;


import com.mthree.bankmthree.dto.account.AccountDTO;
import com.mthree.bankmthree.dto.auth.RegisterRequest;
import com.mthree.bankmthree.dto.user.UserDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.enums.Role;
import com.mthree.bankmthree.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRoleToString")
    @Mapping(target = "password", source = "password", ignore = true)
    UserDTO toUserDTO(User user);

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    User toUser(RegisterRequest userDTO);

    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    @Mapping(target = "expirationDate", source = "expirationDate")
    AccountDTO toAccountDTO(Account account);

    @Named("maskCardNumber")
    default String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** **** **** ****";
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }

    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.name() : null;
    }
}
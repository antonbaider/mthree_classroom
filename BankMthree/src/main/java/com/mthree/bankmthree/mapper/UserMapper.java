package com.mthree.bankmthree.mapper;

import com.mthree.bankmthree.dto.account.AccountDTO;
import com.mthree.bankmthree.dto.auth.RegisterRequest;
import com.mthree.bankmthree.dto.user.UserDTO;
import com.mthree.bankmthree.entity.Account;
import com.mthree.bankmthree.entity.UserProfile;
import com.mthree.bankmthree.entity.enums.Role;
import com.mthree.bankmthree.entity.User;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "email", source = "profile.email")
    @Mapping(target = "phone", source = "profile.phone")
    @Mapping(target = "ssn", source = "profile.ssn")
    @Mapping(target = "username", source = "profile.username")
    @Mapping(target = "password", ignore = true) // if you don't want to expose password in response
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRoleToString")
    UserDTO toUserDTO(User user);

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "profile", source = "registerRequest", qualifiedByName = "mapToUserProfile")
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "type", constant = "STANDARD")
    User toUser(RegisterRequest registerRequest, @Context PasswordEncoder passwordEncoder);

    @Named("mapToUserProfile")
    default UserProfile toUserProfile(RegisterRequest registerRequest, @Context PasswordEncoder passwordEncoder) {
        return UserProfile.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .ssn(registerRequest.getSsn())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // Encode password
                .build();
    }

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
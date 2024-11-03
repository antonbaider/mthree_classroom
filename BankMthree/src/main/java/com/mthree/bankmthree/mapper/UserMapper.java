package com.mthree.bankmthree.mapper;


import com.mthree.bankmthree.dto.UserDTO;
import com.mthree.bankmthree.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}

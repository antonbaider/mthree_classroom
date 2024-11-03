package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.UserDTO;
import com.mthree.bankmthree.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
       // UserDTO newUser = userService.registerUser(userDTO);
        return null;
        //ResponseEntity.ok().body();
    }

//    @PostMapping("/transfer")
//    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest transferRequest) {
//        // Call service method
//    }
}

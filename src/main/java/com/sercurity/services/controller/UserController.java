package com.sercurity.services.controller;

import com.sercurity.services.dtos.UserDto;
import com.sercurity.services.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/hello")
    public String showMsg(){
        return "hello!";
    }




    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
       try{
           return ResponseEntity.ok(userService.createUser(userDto));
       }catch (Exception e){
            return ResponseEntity.status(500).body("Failed to create user " + e.getMessage());
       }

    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){
        try{
            return ResponseEntity.ok(userService.updateUser(userDto));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Failed to update user " + e.getMessage());
        }
    }


    @DeleteMapping ("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto userDto){
        try{
            return ResponseEntity.ok(userService.deleteUser(userDto.getUsername()));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Failed to delete user " + e.getMessage());
        }
    }

}
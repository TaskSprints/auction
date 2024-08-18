package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.UserDto;
import com.tasksprints.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = UserDto.fromEntity(userService.findById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDto);
    }

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        User user = userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        User user = userService.update(id, userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

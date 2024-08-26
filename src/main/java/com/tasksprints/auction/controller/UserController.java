package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.UserDto;
import com.tasksprints.auction.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;
    @Operation(summary = "Get Users", description = "전체 유저들을 조회한다.")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> responseDtos = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @Operation(summary = "Get User", description = "특정 유저를 조회한다.")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto responseDto = userService.fromEntity(userService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Add User", description = "유저를 추가한다.")
    @PostMapping("/user")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        User savedUser = userService.save(userDto);
        UserDto responseDto = userService.fromEntity(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @Operation(summary = "Update User", description = "특정 유저 정보를 수정한다.")
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        User updatedUser = userService.update(id, userDto);
        UserDto responseDto = userService.fromEntity(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Delete User", description = "특정 유저를 삭제한다.")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

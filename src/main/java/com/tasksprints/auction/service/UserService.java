package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.UserDto;
import com.tasksprints.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }
    public User update(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        user.update(userDto.getUsername(),
                userDto.getEmail(),
                userDto.getNickname(),
                userDto.getPhone()
        );
        return userRepository.save(user);
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserDto::fromEntity )
                .toList();

    }
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}

package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.UserDto;
import com.tasksprints.auction.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto fromEntity(User user) {
                return UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .password(user.getPassword())
                        .phone(user.getPhone())
    //                    .created_at(user.getCreatedAt())
    //                    .updated_at(user.getUpdatedAt())
                        .build();
            }

        public User toEntity(UserDto userDto) {
            return User.builder()
                    .id(userDto.getId())
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .nickname(userDto.getNickname())
                    .password(userDto.getPassword())
                    .phone(userDto.getPhone())
    //                .createdAt(this.created_at)
    //                .updatedAt(this.updated_at)
                    .build();

        }
    @Transactional
    public User save(UserDto userDto) {
        User user = this.toEntity(userDto);
        return userRepository.save(user);
    }
    @Transactional
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
                .map(this::fromEntity )
                .toList();
    }
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}

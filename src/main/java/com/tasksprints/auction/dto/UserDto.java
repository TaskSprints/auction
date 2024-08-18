package com.tasksprints.auction.dto;

import com.tasksprints.auction.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phone;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static UserDto fromEntity(User user) {
            return UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .phone(user.getPhone())
                    .created_at(user.getCreatedAt())
                    .updated_at(user.getUpdatedAt())
                    .build();
        }

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .nickname(this.nickname)
                .phone(this.phone)
                .createdAt(this.created_at)
                .updatedAt(this.updated_at)
                .build();

    }
}

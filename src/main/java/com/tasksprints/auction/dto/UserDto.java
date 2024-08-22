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
    private String email;
    private String nickname;
    private String phone;
    private String password;

//    private LocalDateTime created_at;
//    private LocalDateTime updated_at;


}

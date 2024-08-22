package com.tasksprints.auction.dto;


import com.tasksprints.auction.domain.Item;
import com.tasksprints.auction.domain.ItemImage;

import com.tasksprints.auction.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ItemImageDto {

    private Long id;
    private String url;
    private Boolean isPrimary;
//    createdAt, updatedAt은 Dto에서 빼야 한다
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
    private Long itemId;


}

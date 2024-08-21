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
    // 수정 필요
    private final ItemRepository itemRepository;

    private Long id;
    private String url;
    private Boolean isPrimary;
//    createdAt, updatedAt은 Dto에서 빼야 한다
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
    private Long itemId;

    public static ItemImageDto fromEntity(ItemImage itemImage) {
        return ItemImageDto.builder()
                .id(itemImage.getId())
                .url(itemImage.getUrl())
                .isPrimary(itemImage.getIsPrimary())
                .itemId(itemImage.getItem().getId())
                .build();
    }

    public ItemImage toEntity() {
        /*
        // itemId를 사용하여 Item 엔티티 조회. 수정 필요
        * */
           Item item = itemRepository.findById(itemId)
                   .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        return ItemImage.builder()
                .id(id)
                .url(url)
                .isPrimary(isPrimary)
                // 수정 필요
                .item(item)
                //
                .build();
    }
}

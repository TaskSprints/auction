package com.tasksprints.auction.dto;

import com.tasksprints.auction.domain.Auction;
import com.tasksprints.auction.domain.Item;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private List<ItemImageDto> itemImages;

    public static ItemDto fromEntity(Item item) {
           return ItemDto.builder()
                   .id(item.getId())
                   .name(item.getName())
                   .description(item.getDescription())
                   .category(item.getCategory())
                   .itemImages(item.getItemImages().stream()
                           .map(ItemImageDto::fromEntity)
                           .toList())
                   .build();
       }
    public Item toEntity() {
            return Item.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .category(category)
                    .itemImages(itemImages.stream()
                            .map(ItemImageDto::toEntity)
                            .toList())
                    .build();
        }
}

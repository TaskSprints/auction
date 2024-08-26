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

}

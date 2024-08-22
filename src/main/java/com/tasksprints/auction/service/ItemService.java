package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Item;
import com.tasksprints.auction.domain.ItemImage;
import com.tasksprints.auction.dto.ItemDto;
import com.tasksprints.auction.dto.ItemImageDto;
import com.tasksprints.auction.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemDto fromEntity(Item item) {
        List<ItemImageDto> itemImageDtos = item.getItemImages().stream()
                    .map(itemImage -> ItemImageDto.builder()
                            .id(itemImage.getId())
                            .url(itemImage.getUrl())
                            .isPrimary(itemImage.getIsPrimary())
                            .itemId(item.getId())
//                            .itemId(itemImage.getItem().getId())
                            .build())
                .toList();
               return ItemDto.builder()
                       .id(item.getId())
                       .name(item.getName())
                       .description(item.getDescription())
                       .category(item.getCategory())
                        /*
                        아이템은 아이템 이미지를 들고 있어야 하는데 id로 들고 있어도 될 거 같고
                        아이템 이미지들은 아이템을
                        * */
                       .itemImages(itemImageDtos)
                       .build();
           }

    public Item toEntity(ItemDto itemDto) {

        Item item = Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .category(itemDto.getCategory())
//                .itemImages(itemImages)
                .build();


        List<ItemImage> itemImages = itemDto.getItemImages().stream()
                                    .map(itemImageDto -> ItemImage.builder()
                                            .id(itemImageDto.getId())
                                            .url(itemImageDto.getUrl())
                                            .isPrimary(itemImageDto.getIsPrimary())
                                            .item(item)
                                            .build()
                                    )
                                    .toList();

        item.setItemImages(itemImages);

        return item;
    }

    @Transactional
    public Item save(ItemDto itemDto) {
        Item item = this.toEntity(itemDto);
        return itemRepository.save(item);
    }
    public List<ItemDto> findAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(this::fromEntity)
                .toList();
    }
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    }
    @Transactional
    public Item update(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        item.update(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getCategory()
        );
        return itemRepository.save(item);
    }
    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}

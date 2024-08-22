package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Item;
import com.tasksprints.auction.domain.ItemImage;
import com.tasksprints.auction.dto.ItemImageDto;
import com.tasksprints.auction.dto.UserDto;
import com.tasksprints.auction.repository.ItemImageRepository;
import com.tasksprints.auction.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemImageService {
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    public ItemImageDto fromEntity(ItemImage itemImage) {
            return ItemImageDto.builder()
                    .id(itemImage.getId())
                    .url(itemImage.getUrl())
                    .isPrimary(itemImage.getIsPrimary())
                    .itemId(itemImage.getItem().getId())
                    .build();
        }

    public ItemImage toEntity(ItemImageDto itemImageDto) {

       Item item = itemRepository.findById(itemImageDto.getItemId()).orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemImageDto.getItemId()));

        return ItemImage.builder()
                .id(itemImageDto.getId())
                .url(itemImageDto.getUrl())
                .isPrimary(itemImageDto.getIsPrimary())
                .item(item)
                .build();
    }

    @Transactional
    public ItemImage save(Long itemId, ItemImageDto itemImageDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("not found: " + itemId));
        ItemImage itemImage = this.toEntity(itemImageDto);
        item.addItemImage(itemImage);

        return itemImageRepository.save(itemImage);
    }
    @Transactional
    public ItemImage update(Long itemId, Long itemImageId, ItemImageDto itemImageDto) {
        /*
        연관관계가 매핑이 된 상태에서는 update할 때 명시적 함수가 불 필요?
        item과 itemImage는 매핑된 상태에서 itemImage만 수정하면 알아서 수정되니까??
        */
//        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("not found: " + itemId));
        ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(() -> new IllegalArgumentException("not found: " + itemImageId));
        itemImage.update(
                itemImageDto.getUrl(),
                itemImageDto.getIsPrimary()
        );

//        item.updateItemImage(itemImage);

        return itemImageRepository.save(itemImage);
    }
    @Transactional
    public void delete(Long itemId, Long itemImageId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("not found: " + itemId));
        ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(() -> new IllegalArgumentException("not found: " + itemImageId));
        item.removeItemImage(itemImage);

        itemImageRepository.deleteById(itemImageId);
    }
    public ItemImage findById(Long itemImageId) {
        return itemImageRepository.findById(itemImageId).orElseThrow(() -> new IllegalArgumentException("not found: " + itemImageId));
    }
    public List<ItemImageDto> findAll() {
        List<ItemImage> itemImages = itemImageRepository.findAll();
        return itemImages.stream()
                .map(this::fromEntity)
                .toList();
    }
}

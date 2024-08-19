package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Item;
import com.tasksprints.auction.dto.ItemDto;
import com.tasksprints.auction.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Item save(ItemDto itemDto) {
        return itemRepository.save(itemDto.toEntity());
    }
    public List<ItemDto> findAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(ItemDto::fromEntity)
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

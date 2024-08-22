package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.Item;
import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.ItemDto;
import com.tasksprints.auction.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> getAllItems() {
        List<ItemDto> responseDtos = itemService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @GetMapping("/item/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        ItemDto responseDto = itemService.fromEntity(itemService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PostMapping("/item")
    public ResponseEntity<ItemDto> addItem(@RequestBody ItemDto itemDto) {
        Item savedItem = itemService.save(itemDto);
        ItemDto responseDto = itemService.fromEntity(savedItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PutMapping("/item/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        Item updatedItem = itemService.update(id, itemDto);
        ItemDto responseDto = itemService.fromEntity(updatedItem);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

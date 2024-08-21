package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.ItemImage;
import com.tasksprints.auction.dto.ItemImageDto;
import com.tasksprints.auction.service.ItemImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item/{itemId}")
@RequiredArgsConstructor
public class ItemImageController {
    private final ItemImageService itemImageService;

    @GetMapping("/images")
    public ResponseEntity<List<ItemImageDto>> getAllImages() {
        List<ItemImageDto> responseDtos = itemImageService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @GetMapping("/image/{imageId}")
    public ResponseEntity<ItemImageDto> getImage(@PathVariable Long itemId, @PathVariable Long imageId) {
        ItemImageDto responseDto = ItemImageDto.fromEntity(itemImageService.findById(imageId));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PostMapping("/image")
    public ResponseEntity<ItemImageDto> addImage(@RequestBody ItemImageDto itemImageDto, @PathVariable Long itemId) {
        ItemImage savedItemImage = itemImageService.save(itemId, itemImageDto);
        ItemImageDto responseDto = ItemImageDto.fromEntity(savedItemImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    @PutMapping("/image/{imageId}")
    public ResponseEntity<ItemImageDto> updateImage(@RequestBody ItemImageDto itemImageDto,
                                                    @PathVariable Long itemId,
                                                    @PathVariable Long imageId) {
        ItemImage updatedItemImage = itemImageService.update(itemId, imageId, itemImageDto);
        ItemImageDto responseDto = ItemImageDto.fromEntity(updatedItemImage);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<ItemImageDto> deleteImage(@PathVariable Long itemId, @PathVariable Long imageId) {
        itemImageService.delete(itemId, imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

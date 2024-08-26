package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.ItemImage;
import com.tasksprints.auction.dto.ItemImageDto;
import com.tasksprints.auction.service.ItemImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item/{itemId}")
@RequiredArgsConstructor
@Tag(name = "ItemImage", description = "ItemImage API")
public class ItemImageController {
    private final ItemImageService itemImageService;

    @Operation(summary = "Get ItemImages by itemID", description = "특정 상품에 대한 모든 상품 이미지를 조회한다.")
    @GetMapping("/images")
    public ResponseEntity<List<ItemImageDto>> getAllImages(Long itemId) {
        List<ItemImageDto> responseDtos = itemImageService.findAll(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @Operation(summary = "Get ItemImage by imageId", description = "특정 상품에 대한 특정 상품 이미지를 조회한다.")
    @GetMapping("/image/{imageId}")
    public ResponseEntity<ItemImageDto> getImage(@PathVariable Long itemId, @PathVariable Long imageId) {
        ItemImageDto responseDto = itemImageService.fromEntity(itemImageService.findById(imageId));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Add ItemImage to itemID", description = "특정 상품에 대한 상품 이미지를 추가한다.")
    @PostMapping("/image")
    public ResponseEntity<ItemImageDto> addImage(@RequestBody ItemImageDto itemImageDto, @PathVariable Long itemId) {
        ItemImage savedItemImage = itemImageService.save(itemId, itemImageDto);
        ItemImageDto responseDto = itemImageService.fromEntity(savedItemImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    @Operation(summary = "Update ItemImage to itemID", description = "특정 상품에 대한 상품 이미지를 수정한다.")
    @PutMapping("/image/{imageId}")
    public ResponseEntity<ItemImageDto> updateImage(@RequestBody ItemImageDto itemImageDto,
                                                    @PathVariable Long imageId) {
        ItemImage updatedItemImage = itemImageService.update(imageId, itemImageDto);
        ItemImageDto responseDto = itemImageService.fromEntity(updatedItemImage);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Delete ItemImage to itemID", description = "특정 상품에 대한 상품 이미지를 삭제한다.")
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<ItemImageDto> deleteImage(@PathVariable Long itemId, @PathVariable Long imageId) {
        itemImageService.delete(itemId, imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

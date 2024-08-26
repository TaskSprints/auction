package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.Auction;
import com.tasksprints.auction.dto.AuctionDto;
import com.tasksprints.auction.repository.AuctionRepository;
import com.tasksprints.auction.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Auction", description = "Auction API")
public class AuctionController {
    private final AuctionService auctionService;

    @Operation(summary = "Get Auctions", description = "모든 경매를 조회한다.")
    @GetMapping("/auctions")
    public ResponseEntity<List<AuctionDto>> getAllAuctions() {
        List<AuctionDto> auctions = auctionService.findAllAuctions();
        return ResponseEntity.status(HttpStatus.OK).body(auctions);
    }
    @Operation(summary = "Get Auction", description = "특정 경매를 조회한다.")
    @GetMapping("/auction/{id}")
    public ResponseEntity<AuctionDto> getAuctionById(@PathVariable Long id) {
        Auction findAuction = auctionService.findById(id);
        AuctionDto responseDto = auctionService.fromEntity(findAuction);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Add Auction", description = "경매를 추가한다.")
    @PostMapping("/auction")
    public ResponseEntity<AuctionDto> addAuction(@RequestBody AuctionDto auctionDto) {
        Auction addAuction = auctionService.save(auctionDto);
        AuctionDto responseDto = auctionService.fromEntity(addAuction);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @Operation(summary = "Update Auction", description = "특정 경매를 수정한다.")
    @PutMapping("/auction/{id}")
    public ResponseEntity<AuctionDto> updateAuction(@PathVariable Long id, @RequestBody AuctionDto auctionDto) {
        Auction updateAuction = auctionService.update(id, auctionDto);
        AuctionDto responseDto = auctionService.fromEntity(updateAuction);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Delete Auction", description = "특정 경매를 삭제한다.")
    @DeleteMapping("/auction/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        auctionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

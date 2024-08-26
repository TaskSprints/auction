package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.Bid;
import com.tasksprints.auction.dto.BidDto;
import com.tasksprints.auction.repository.BidRepository;
import com.tasksprints.auction.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auction/{auctionId}")
@RequiredArgsConstructor
@Tag(name = "Bid", description = "Bid API")
public class BidController {
    private final BidService bidService;
    @Operation(summary = "Get Bids", description = "특정 경매에 대한 모든 입찰을 조회한다.")
    @GetMapping("/bids")
    public ResponseEntity<List<BidDto>> getAllBids(@PathVariable Long auctiondId) {
        List<BidDto> responseDtos = bidService.findAllBids(auctiondId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @Operation(summary = "Get Bid", description = "특정 경매에 대한 특정 입찰을 조회한다.")
    @GetMapping("/bid/{bidId}")
    public ResponseEntity<BidDto> getBid(@PathVariable Long bidId) {
        Bid bid = bidService.findById(bidId);
        BidDto bidDto = bidService.fromEntity(bid);
        return ResponseEntity.status(HttpStatus.OK).body(bidDto);
    }
    @Operation(summary = "Add Bid", description = "특정 경매에 대해 입찰한다.")
    @PostMapping("/bid")
    public ResponseEntity<BidDto> addBid(@RequestBody BidDto bidDto) {
        Bid bid = bidService.save(bidDto);
        BidDto responseDto = bidService.fromEntity(bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @Operation(summary = "Update Bid", description = "특정 경매에 대한 입찰을 수정한다.")
    @PutMapping("/bid/{bidId}")
    public ResponseEntity<BidDto> updateBid(@PathVariable Long bidId, @RequestBody BidDto bidDto) {
        Bid bid = bidService.update(bidId, bidDto);
        if (bid == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BidDto responseDto = bidService.fromEntity(bid);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @Operation(summary = "Delete Bid", description = "특정 경매에 대한 입찰을 삭제한다.")
    @DeleteMapping("/bid/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long bidId) {
        bidService.delete(bidId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.Bid;
import com.tasksprints.auction.dto.BidDto;
import com.tasksprints.auction.repository.BidRepository;
import com.tasksprints.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @GetMapping("/bids")
    public ResponseEntity<List<BidDto>> getAllBids() {
        List<BidDto> responseDtos = bidService.findAllBids();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @GetMapping("/bid/{bidId}")
    public ResponseEntity<BidDto> getBid(@PathVariable Long bidId) {
        Bid bid = bidService.findById(bidId);
        BidDto bidDto = bidService.fromEntity(bid);
        return ResponseEntity.status(HttpStatus.OK).body(bidDto);
    }
    @PostMapping("/bid")
    public ResponseEntity<BidDto> addBid(@RequestBody BidDto bidDto) {
        Bid bid = bidService.save(bidDto);
        BidDto responseDto = bidService.fromEntity(bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PutMapping("/bid/{bidId}")
    public ResponseEntity<BidDto> updateBid(@PathVariable Long bidId, @RequestBody BidDto bidDto) {
        Bid bid = bidService.update(bidId, bidDto);
        if (bid == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BidDto responseDto = bidService.fromEntity(bid);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/bid/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long bidId) {
        bidService.delete(bidId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

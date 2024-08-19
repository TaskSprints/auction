package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Auction;
import com.tasksprints.auction.dto.AuctionDto;
import com.tasksprints.auction.repository.AuctionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public List<AuctionDto> findAllAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        return auctions.stream()
                .map(AuctionDto::fromEntity)
                .toList();
    }
    public Auction findById(Long id) {
        return auctionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    @Transactional
    public Auction save(AuctionDto auctionDto) {
        return auctionRepository.save(auctionDto.toEntity());
    }
    @Transactional
    public Auction update(Long id, AuctionDto auctionDto) {
        Auction auction = auctionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        auction.update(
                auctionDto.getStartTime(),
                auctionDto.getEndTime(),
                auctionDto.getStartingBid(),
                auctionDto.getHighestBid(),
                auctionDto.getBidderId()
        );
        return auctionRepository.save(auction);
    }
    @Transactional
    public void delete(Long id) {auctionRepository.deleteById(id);}
}

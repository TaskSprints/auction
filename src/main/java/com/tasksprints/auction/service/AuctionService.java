package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Auction;
import com.tasksprints.auction.domain.Item;
import com.tasksprints.auction.dto.AuctionDto;
import com.tasksprints.auction.repository.AuctionRepository;
import com.tasksprints.auction.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
    public  AuctionDto fromEntity(Auction auction) {
            return AuctionDto.builder()
                    .id(auction.getId())
                    .startTime(auction.getStartTime())
                    .endTime(auction.getEndTime())
                    .startingBid(auction.getStartingBid())
                    .highestBid(auction.getHighestBid())
                    .bidderId(auction.getBidderId())
                    .itemId(auction.getId())
                    .build();
        }
    public Auction toEntity(AuctionDto auctionDto) {
        Item item = itemRepository.findById(auctionDto.getItemId()).orElseThrow(() -> new RuntimeException("Auction not found with id " + auctionDto.getItemId()));
        return Auction.builder()
                    .id(auctionDto.getId())
                    .startTime(auctionDto.getStartTime())
                    .endTime(auctionDto.getEndTime())
                    .startingBid(auctionDto.getStartingBid())
                    .highestBid(auctionDto.getHighestBid())
                    .bidderId(auctionDto.getBidderId())
                    .item(item)
                    .build();
        }

    public List<AuctionDto> findAllAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        return auctions.stream()
                .map(this::fromEntity)
                .toList();
    }
    public Auction findById(Long id) {
        return auctionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    @Transactional
    public Auction save(AuctionDto auctionDto) {
        Auction auction = this.toEntity(auctionDto);
        return auctionRepository.save(auction);
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

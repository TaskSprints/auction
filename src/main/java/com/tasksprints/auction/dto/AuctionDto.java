package com.tasksprints.auction.dto;

import com.tasksprints.auction.domain.Auction;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long startingBid;
    private Long highestBid;
    private String bidderId;

    public static AuctionDto fromEntity(Auction auction) {
        return AuctionDto.builder()
                .id(auction.getId())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .startingBid(auction.getStartingBid())
                .highestBid(auction.getHighestBid())
                .bidderId(auction.getBidderId())
                .build();
    }
    public Auction toEntity() {
        return Auction.builder()
                .id(id)
                .startTime(startTime)
                .endTime(endTime)
                .startingBid(startingBid)
                .highestBid(highestBid)
                .bidderId(bidderId)
                .build();
    }
}

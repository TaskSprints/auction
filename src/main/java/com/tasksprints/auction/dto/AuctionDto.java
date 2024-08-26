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
    private Long highestBidAmount;
    private Long highestBidId;
    private Long itemId;



}

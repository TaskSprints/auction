package com.tasksprints.auction.auction.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class AuctionClosedEvent {
    private final Long auctionId;
    private final Long highestBidderId;
    private final BigDecimal highestBidAmount;
}

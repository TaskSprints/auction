package com.tasksprints.auction.common.jobrunr;

import com.tasksprints.auction.auction.domain.entity.Auction;
import com.tasksprints.auction.auction.domain.event.AuctionClosedEvent;
import com.tasksprints.auction.auction.exception.AuctionNotFoundException;
import com.tasksprints.auction.auction.infrastructure.AuctionRepository;
import com.tasksprints.auction.bid.domain.entity.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AuctionJob {
    private final AuctionRepository auctionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 경매 시작 상태 변경
    @Transactional
    public void startAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Not Found Auction ID: " + auctionId));
        auction.activate();
        System.out.println("경매 시작 - ID: " + auctionId);
    }

    // 경매 종료 상태 변경
    @Transactional
    public void endAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Not Found Auction ID: " + auctionId));
        auction.close();
        //잔액 차감 이벤트 요청
        Bid bid = auction.getBids().getFirst();
        AuctionClosedEvent event = new AuctionClosedEvent(auctionId, bid.getId(), bid.getAmount());
        applicationEventPublisher.publishEvent(event);
        System.out.println("경매 종료 - ID: " + auctionId);
    }
}

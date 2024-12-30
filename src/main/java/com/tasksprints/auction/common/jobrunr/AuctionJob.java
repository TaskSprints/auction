package com.tasksprints.auction.common.jobrunr;

import com.tasksprints.auction.auction.domain.entity.Auction;
import com.tasksprints.auction.auction.exception.AuctionNotFoundException;
import com.tasksprints.auction.auction.infrastructure.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuctionJob {
    private final AuctionRepository auctionRepository;

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
        System.out.println("경매 종료 - ID: " + auctionId);
    }
}

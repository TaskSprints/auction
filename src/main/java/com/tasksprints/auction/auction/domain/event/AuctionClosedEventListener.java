package com.tasksprints.auction.auction.domain.event;

import com.tasksprints.auction.wallet.domain.entity.Wallet;
import com.tasksprints.auction.wallet.infrastructure.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionClosedEventListener {
    private final WalletRepository walletRepository;

    /**
     * 비동기로 지갑 잔액 차감 처리
     * 동기로 처리하게 되면 이벤트 처리 할 때까지 경매의 close 상태를 업데이트 커밋하지 않음(트랜잭션 커밋)
     * 경매를 닫고(트랜잭션 끝내고), 비동기로 잔액 차감을 처리해도 문제 없다..
     */
    @Async("asyncExecutor")
    @EventListener
    @Transactional
    public void handleAuctionClosed(AuctionClosedEvent event) {
        log.info("경매 종료 이벤트 처리: auctionId={}", event.getAuctionId());
        try {
            Wallet winnerWallet = walletRepository.getWalletByUserId(event.getHighestBidderId());
            winnerWallet.deductBalance(event.getHighestBidAmount());
            walletRepository.save(winnerWallet);
            log.info("지갑 잔액 차감 성공: auctionId={}", event.getAuctionId());
        } catch (Exception e) {
            log.error("지갑 잔액 차감 실패: auctionId={}, error={}", event.getAuctionId(), e.getMessage());
        }
    }

}

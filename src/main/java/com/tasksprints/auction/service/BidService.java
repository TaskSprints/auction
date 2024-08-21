package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Auction;
import com.tasksprints.auction.domain.Bid;
import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.BidDto;
import com.tasksprints.auction.repository.AuctionRepository;
import com.tasksprints.auction.repository.BidRepository;
import com.tasksprints.auction.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public BidDto fromEntity(Bid bid) {
            return BidDto.builder()
                    .id(bid.getId())
                    .bidAmount(bid.getBidAmount())
                    .bidTime(bid.getBidTime())
                    .auctionId(bid.getAuction().getId())
                    .userId(bid.getUser().getId())
                    .build();

        }

    public  Bid toEntity(BidDto bidDto) {
    Auction auction = auctionRepository.findById(bidDto.getAuctionId())
           .orElseThrow(() -> new RuntimeException("Auction not found with id " + bidDto.getAuctionId()));
    User user = userRepository.findById(bidDto.getUserId())
           .orElseThrow(() -> new RuntimeException("User not found with id " + bidDto.getUserId()));
        return Bid.builder()
                .bidAmount(bidDto.getBidAmount())
                .bidTime(bidDto.getBidTime())
                .auction(auction)
                .user(user)
                .build();
    }

    public List<BidDto> findAllBids() {
        List<Bid> bids = bidRepository.findAll();
        return bids.stream()
                .map(this::fromEntity)
                .toList();
    }
    public Bid findById(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new IllegalArgumentException("not found: " + bidId));
    }

    //  어떤 경매에 대해서 bid 신청
    // dto는 id 값들을 갖고 있고, dto->entity 과정에서 auction과 user와의 매핑이 됨
    @Transactional
    public Bid save(BidDto bidDto) {
        Bid bid = this.toEntity(bidDto);
        return bidRepository.save(bid);
    }

//  어떤 경매에 대해서 행한 bid
    @Transactional
    public Bid update(Long bidId, BidDto bidDto) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new IllegalArgumentException("not found: " + bidId));
        // 매핑된 auction, user는 바뀌지 않아서..
        bid.update(
                bidDto.getBidAmount(),
                bidDto.getBidTime()
        );
        return bidRepository.save(bid);
    }
//  어떤 경매에 대해서 행한 bid를 제거
    @Transactional
    public void delete(Long bidId) {
        bidRepository.deleteById(bidId);
    }
}

package com.tasksprints.auction.auction.domain.entity;

import com.tasksprints.auction.auction.exception.InvalidAuctionStateException;
import com.tasksprints.auction.bid.domain.entity.Bid;
import com.tasksprints.auction.common.entity.BaseEntity;
import com.tasksprints.auction.product.domain.entity.Product;
import com.tasksprints.auction.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLRestriction("closed_at is null")
@ToString
@Entity(name = "auction")
public class Auction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionCategory auctionCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private AuctionStatus auctionStatus;

    @Column(nullable = false)
    private BigDecimal startingBid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    @Builder.Default
    private Product product = null;

    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Bid> bids = new ArrayList<>();

    @Column(nullable = true, name= "closed_at")
    private LocalDateTime closedAt;

    public static Auction create(LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingBid, AuctionCategory auctionCategory, AuctionStatus auctionStatus, User seller) {
        Auction newAuction = Auction.builder()
            .startTime(startTime)
            .endTime(endTime)
            .startingBid(startingBid)
            .auctionCategory(auctionCategory)
            .auctionStatus(auctionStatus)
            .build();
        newAuction.addUser(seller);
        return newAuction;
    }

    public void addProduct(Product product) {
        //product.addAuction(this); product에서 auction을 추가하고 있어서 중복
        this.product = product;
    }

    public void addUser(User seller) {
        seller.addAuction(this);
        this.seller = seller;
    }

    public void activate() {
        canActivate();
        this.auctionStatus = AuctionStatus.ACTIVE;
    }

    public void close() {
        canClose();
        this.auctionStatus = AuctionStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    private void canActivate() {
        if (!auctionStatus.equals(AuctionStatus.PENDING)) {
            throw new InvalidAuctionStateException("Auction state change error: Can only start an auction from PENDING state.");
        }
    }

    private void canClose() {
        if (!auctionStatus.equals(AuctionStatus.ACTIVE)) {
            throw new InvalidAuctionStateException("Auction state change error: Can only close an auction from ACTIVE state.");
        }
    }


}

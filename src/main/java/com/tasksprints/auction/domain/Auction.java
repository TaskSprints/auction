package com.tasksprints.auction.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "auction_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Long startingBid;

    @Column(nullable = false)
    private Long highestBid;

    @Column()
    private String bidderId;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status; //[UPCOMING, ONGOING, SOLD, UNSOLD, CANCELED]

    @Enumerated(EnumType.STRING)
    private AuctionCategory category; //[OPEN_PAID, OPEN_FREE, SEALED_BID]

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void update(LocalDateTime startTime, LocalDateTime endTime, Long startingBid, Long highestBid, String bidderId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingBid = startingBid;
        this.highestBid = highestBid;
        this.bidderId = bidderId;
    }

    /*
    추가로 즐겨찾기, 리뷰를 fk로 연결해야 한다.
    * */
}

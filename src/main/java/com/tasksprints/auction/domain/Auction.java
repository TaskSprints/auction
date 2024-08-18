package com.tasksprints.auction.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
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

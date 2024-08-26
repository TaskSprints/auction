package com.tasksprints.auction.dto;

import com.tasksprints.auction.domain.Bid;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidDto {
    private Long id;
    private long bidAmount;
    private LocalDateTime bidTime;
    private Long auctionId;
    private Long userId;

}

package com.tasksprints.auction.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private long reviewRating;
    private String reviewComment;
    private Long userId;
    private Long auctionId;
}

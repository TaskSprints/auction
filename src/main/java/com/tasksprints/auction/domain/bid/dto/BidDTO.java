package com.tasksprints.auction.domain.bid.dto;

import com.tasksprints.auction.domain.bid.model.Bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO {
    String uuid;
    String name;
    Long auctionId;
    BigDecimal amount;

    public static BidDTO of(Bid bid){
        return BidDTO.builder()
                .uuid(bid.getUuid())
                .name(bid.getUser().getName())
                .auctionId(bid.getAuction().getId())
                .amount(bid.getAmount())
                .build();
        /** 아이템 목록 추가**/
    }
}

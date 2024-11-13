package com.tasksprints.auction.domain.chat.dto;

import lombok.Data;

public class ChatRequest {
    @Data
    public static class Create{
        Long auctionId;
    }
    @Data
    public static class Enter{
        Long chatroomId;
        Long userId;
    }
}

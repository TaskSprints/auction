package com.tasksprints.auction.domain;

public enum AuctionStatus {
    /**
        * 경매 시작 전
        */
       UPCOMING,
       /**
        * 현재 진행 중인 경매
        */
       ONGOING,
       /**
        * 낙찰된 경매 상태
        */
       SOLD,
       /**
        * 유찰된 경매 상태
        */
       UNSOLD,
       /**
        * 경매가 취소된 상태
        */
       CANCELLED
}

package com.tasksprints.auction.domain.auction.repository.support;

import com.tasksprints.auction.domain.auction.dto.request.AuctionRequest;
import com.tasksprints.auction.domain.auction.dto.response.AuctionResponse;

import com.tasksprints.auction.domain.auction.model.Auction;
import com.tasksprints.auction.domain.product.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AuctionCriteriaRepository {
    Page<Auction> getAuctionsByFilters(Pageable pageable, AuctionRequest.SearchCondition searchCondition);
    @Deprecated
    Page<Auction> getAuctionsByCategory(Pageable pageable,
                                                AuctionRequest.SearchCondition searchCondition,
                                                ProductCategory category);
}

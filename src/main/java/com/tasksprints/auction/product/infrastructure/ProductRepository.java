package com.tasksprints.auction.product.infrastructure;

import com.tasksprints.auction.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByAuctionId(Long auctionId);
    //컬렉션까지 Fetch Join 하거나, 컬렉션은 지연 로딩 초기화로 하는 방식 트레이드 오프 고려하기
    @Query("SELECT DISTINCT p FROM products p JOIN FETCH p.owner o LEFT JOIN FETCH p.productImageList pi WHERE p.auction.id = :auctionId")
    Optional<Product> findByAuctionIdV2(@Param("auctionId")Long auctionId);
    // 쿼리를 메서드 이름으로 표현
    List<Product> findByOwnerId(Long ownerId);
}

package com.tasksprints.auction.common.initializer;

import com.tasksprints.auction.auction.application.service.AuctionScheduleService;
import com.tasksprints.auction.auction.domain.entity.Auction;
import com.tasksprints.auction.auction.domain.entity.AuctionCategory;
import com.tasksprints.auction.auction.domain.entity.AuctionStatus;
import com.tasksprints.auction.auction.infrastructure.AuctionRepository;
import com.tasksprints.auction.bid.domain.entity.Bid;
import com.tasksprints.auction.bid.infrastructure.BidRepository;
import com.tasksprints.auction.product.domain.entity.Product;
import com.tasksprints.auction.product.domain.entity.ProductImage;
import com.tasksprints.auction.product.infrastructure.ProductImageRepository;
import com.tasksprints.auction.product.infrastructure.ProductRepository;
import com.tasksprints.auction.user.domain.entity.User;
import com.tasksprints.auction.user.infrastructure.UserRepository;
import com.tasksprints.auction.wallet.domain.entity.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AuctionInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final AuctionScheduleService auctionScheduleService; // 스케줄 서비스 추가
    private final BidRepository bidRepository;

    public AuctionInitializer(UserRepository userRepository, AuctionRepository auctionRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, AuctionScheduleService auctionScheduleService, BidRepository bidRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.auctionScheduleService = auctionScheduleService;
        this.bidRepository = bidRepository;
    }

    private void createDummyUser() {
        User user1 = User.createWithWallet("name", "email@email.com", "password", "NickName");
        userRepository.save(user1);
    }

    private Auction createDummyAuction(User user, LocalDateTime startTime, LocalDateTime endTime) {
        Auction auction = Auction.builder()
                .startTime(startTime)
                .endTime(endTime)
                .startingBid(BigDecimal.TEN)
                .auctionCategory(AuctionCategory.PUBLIC_PAID)
                .auctionStatus(AuctionStatus.PENDING)
                .build();
        auction.addUser(user);

        return auctionRepository.save(auction);
    }

    private void createDummyProduct(User user, Auction auction) {
        ProductImage productImage = ProductImage.create("https://sb.kaleidousercontent.com/67418/960x650/77e3d95435/e-commerce-1.png");
        // Save the productImage to avoid the TransientObjectException
        productImageRepository.save(productImage);

        Product product = Product.create("name", "description", user, auction, "헤어", List.of(productImage));
        productRepository.save(product);
    }

    private void createDummyBid(Auction auction) {
        Bid bid = Bid.builder()
            .uuid(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(100))
            .auction(auction)
            .build();
        bidRepository.save(bid);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 각 제품에 대해 새로운 경매를 생성
        for (int i = 0; i < 50; i++) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plus(Duration.ofMillis(15000 + i * 500)); // 시작 시간: 15초 텀을 두고 0.5 초 간격
            LocalDateTime endTime = startTime.plusSeconds(15); // 종료 시간: 시작 후 15초

            User user = createUserWithWallet(i);
            Auction auction = createDummyAuction(user, startTime, endTime);
            createDummyProduct(user, auction);
            createDummyBid(auction);

            auctionScheduleService.scheduleStart(auction.getId(), startTime);
            auctionScheduleService.scheduleEnd(auction.getId(), endTime);
        }
    }

    private User createUserWithWallet(int i) {
        User user = User.builder()
                .name("name" + i)
                .email("email" + i + "@email.com")
                .password("password")
                .nickName("NickName" + i)
                .build();

        Wallet wallet = Wallet.builder()
                .user(user)
                .userName("name" + i)
                .balance(BigDecimal.valueOf(100000.0))
                .build();

        user.addWallet(wallet);
        return userRepository.save(user);
    }
}

package com.tasksprints.auction.common.initializer;

import com.tasksprints.auction.auction.application.service.AuctionScheduleService;
import com.tasksprints.auction.auction.domain.entity.Auction;
import com.tasksprints.auction.auction.domain.entity.AuctionCategory;
import com.tasksprints.auction.auction.domain.entity.AuctionStatus;
import com.tasksprints.auction.auction.infrastructure.AuctionRepository;
import com.tasksprints.auction.product.domain.entity.Product;
import com.tasksprints.auction.product.domain.entity.ProductImage;
import com.tasksprints.auction.product.infrastructure.ProductImageRepository;
import com.tasksprints.auction.product.infrastructure.ProductRepository;
import com.tasksprints.auction.user.domain.entity.User;
import com.tasksprints.auction.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuctionInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final AuctionScheduleService auctionScheduleService; // 스케줄 서비스 추가

    public AuctionInitializer(UserRepository userRepository, AuctionRepository auctionRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, AuctionScheduleService auctionScheduleService) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.auctionScheduleService = auctionScheduleService;
    }

    private void createDummyUser() {
        User user1 = User.createWithWallet("name", "email@email.com", "password", "NickName");
        userRepository.save(user1);
    }

    private Auction createDummyAuction(User user, LocalDateTime startTime, LocalDateTime endTime) {
        Auction auction = Auction.create(startTime, endTime, BigDecimal.TEN, AuctionCategory.PRIVATE_FREE, AuctionStatus.PENDING, user);
        return auctionRepository.save(auction);
    }

    private void createDummyProduct(User user, Auction auction) {
        ProductImage productImage = ProductImage.create("https://sb.kaleidousercontent.com/67418/960x650/77e3d95435/e-commerce-1.png");
        // Save the productImage to avoid the TransientObjectException
        productImageRepository.save(productImage);

        Product product = Product.create("name", "description", user, auction, "헤어", List.of(productImage));
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        User user = userRepository.save(User.createWithWallet("name", "email@email.com", "password", "NickName"));
        LocalDateTime now = LocalDateTime.now();
        // 각 제품에 대해 새로운 경매를 생성
        for (int i = 0; i < 50; i++) {
            Thread.sleep(100);
            LocalDateTime startTime = now.plusSeconds(i * 2); // 시작 시간: 2초 간격
            LocalDateTime endTime = startTime.plusSeconds(15); // 종료 시간: 시작 후 15초

            Auction auction = createDummyAuction(user, startTime, endTime);
            createDummyProduct(user, auction);

            auctionScheduleService.scheduleStart(auction.getId(), startTime);
            auctionScheduleService.scheduleEnd(auction.getId(), endTime);
        }
    }
}

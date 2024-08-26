package com.tasksprints.auction.service;

import com.tasksprints.auction.domain.Auction;
import com.tasksprints.auction.domain.Review;
import com.tasksprints.auction.domain.User;
import com.tasksprints.auction.dto.ReviewDto;
import com.tasksprints.auction.repository.AuctionRepository;
import com.tasksprints.auction.repository.ReviewRepository;
import com.tasksprints.auction.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    public ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .reviewRating(review.getReviewRating())
                .reviewComment(review.getReviewComment())
                .auctionId(review.getAuction().getId())
                .userId(review.getUser().getId())
                .build();
    }
    public Review toEntity(ReviewDto reviewDto) {
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("not found: " + reviewDto.getUserId()));
        Auction auction = auctionRepository.findById(reviewDto.getAuctionId())
                .orElseThrow(() -> new IllegalArgumentException("not found: " + reviewDto.getAuctionId()));
        return Review.builder()
                .id(reviewDto.getId())
                .reviewRating(reviewDto.getReviewRating())
                .reviewComment(reviewDto.getReviewComment())
//                @CreatedDate에서 엔티티 변환 시점에 알아서 넣어줄 것
//                .reviewCreatedAt(LocalDateTime.now())
                .auction(auction)
                .user(user)
                .build();
    }

    //    public List<ReviewDto> findAllByAuctionId(Long auctionId) {
    //        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("not found: " + auctionId));
    //        List<Review> reviews = auction.getReview();
    //        return reviews.stream()
    //                .map(this::fromEntity)
    //                .toList();
    //    }

    @Transactional
    public Review save(Long auctionId, ReviewDto reviewDto) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("not found: " + auctionId));
        Review review = this.toEntity(reviewDto);
        auction.addReview(review);
        return reviewRepository.save(review);
    }
    public List<ReviewDto> findAllByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found: " + userId));

        List<Review> reviews = user.getReviews();
        return reviews.stream()
                .map(this::fromEntity)
                .toList();
    }

    public Review findByAuctionId(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("not found: " + auctionId));
        Review review = auction.getReview();
        if (review == null) {
                throw new IllegalArgumentException("No review found for auction with id: " + auctionId);
        }
        return review;
    }

    @Transactional
    public Review update(Long auctionId, ReviewDto reviewDto) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("not found: " + auctionId));
        Review review = auction.getReview();
        review.update(
                reviewDto.getReviewRating(),
                reviewDto.getReviewComment()
        );
        return reviewRepository.save(review);
    }

    @Transactional
    public void delete(Long auctionId, Long reviewId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("not found: " + auctionId));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("not found: " + reviewId));
        auction.removeReview(review);
        reviewRepository.deleteById(reviewId);
    }
//    아래 코드도 가능한지?
//    @Transactional
//    public void delete(Long auctionId) {
//        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("not found: " + auctionId));
//        Review review = auction.getReview();
//        auction.removeReview(review);
//    }
//      아래 코드도 가능한지?
//    @Transactional
//        public void delete(Long reviewId) {
//            reviewRepository.deleteById(reviewId);
    //    }
}

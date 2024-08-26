package com.tasksprints.auction.controller;

import com.tasksprints.auction.domain.Review;
import com.tasksprints.auction.dto.ReviewDto;
import com.tasksprints.auction.service.AuctionService;
import com.tasksprints.auction.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auction/{auctionId}")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review API")
public class ReviewController {
    private final ReviewService reviewService;

//  auction에 있는 모든 리뷰 조회(auction당 리뷰는 1개..)
//    @GetMapping("/reviews")
//    public ResponseEntity<List<ReviewDto>> getAllReviews(Long auctionId) {
//        List<ReviewDto> reviewDtos = reviewService.findAllByUserId(auctionId);
//        return ResponseEntity.status(HttpStatus.OK).body(reviewDtos);
//    }

    @Operation(summary = "Get Review by AuctionID", description = "특정 경매에 대한 리뷰를 조회한다.")
    @GetMapping("/review")
    public ResponseEntity<ReviewDto> getReviewByAuctionId(@PathVariable Long auctionId) {
        Review review = reviewService.findByAuctionId(auctionId);
        ReviewDto reviewDto = reviewService.fromEntity(review);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }
    @Operation(summary = "Add Review to AuctionID", description = "특정 경매에 대한 리뷰를 조회한다.")
    @PostMapping("/review")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long auctionId, @RequestBody ReviewDto reviewDto) {
        Review review = reviewService.save(auctionId, reviewDto);
        ReviewDto responseDto = reviewService.fromEntity(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update Review to AuctionID", description = "특정 경매에 대한 리뷰를 수정한다.")
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDto reviewDto) {
        Review updatedReview = reviewService.update(reviewId, reviewDto);
        ReviewDto responseDto = reviewService.fromEntity(updatedReview);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "Delete Review by AuctionID and ReviewID", description = "특정 경매에 대한 리뷰를 삭제한다.")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long auctionId, @PathVariable Long reviewId) {
        reviewService.delete(auctionId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.tasksprints.auction.api.bid;

import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.bid.dto.BidRequest;
import com.tasksprints.auction.domain.bid.dto.BidResponse;
import com.tasksprints.auction.domain.bid.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/bid")
public class BidController {
    private final BidService bidService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/bid")
    public Mono<Void> handleBid(BidRequest bidRequest) {
        return Mono.fromCallable(() -> {
            BidResponse bidResponse = bidService.submitBid(bidRequest.getUserId(), bidRequest.getAuctionId(), bidRequest.getAmount());
            log.info(String.valueOf(bidResponse));
            log.info(bidResponse.getUuid());

            simpMessageSendingOperations.convertAndSend("/bid/" + bidResponse.getUuid(), bidResponse);
            return bidResponse;
        }).then();
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get a bid", description = "Get a bid by bid uuid")
    @ApiResponse(responseCode = "200", description = "Bid status retrieved successfully")
    public Mono<ResponseEntity<ApiResult<BidResponse>>> getBidByUuid(@PathVariable(value = "uuid") String uuid) {
        return Mono.fromCallable(() -> {
            BidResponse bid = bidService.getBidByUuid(uuid);
            return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.AUCTION_RETRIEVED, bid));
        });
    }
}

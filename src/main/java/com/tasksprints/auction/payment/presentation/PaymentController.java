package com.tasksprints.auction.payment.presentation;

import com.tasksprints.auction.auth.domain.model.Accessor;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.jwt.Auth;
import com.tasksprints.auction.common.jwt.UserOnly;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.payment.api.Response;
import com.tasksprints.auction.payment.domain.dto.request.PaymentRequest;
import com.tasksprints.auction.payment.domain.dto.response.PaymentResponse;
import com.tasksprints.auction.payment.exception.InvalidSessionException;
import com.tasksprints.auction.payment.exception.PaymentDataMismatchException;
import com.tasksprints.auction.payment.application.service.PaymentService;
import com.tasksprints.auction.payment.exception.RedisKeyNotFoundException;
import com.tasksprints.auction.payment.infrastructure.redis.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final RedisService redisService;

    @PostMapping("/prepare")
    @Operation(summary = "Temporarily stores the payment element", description = "Save orderID and amount in session")
    @ApiResponse(responseCode = "200", description = "Payment prepared successfully")
    public ResponseEntity<ApiResult<String>> preparePayment(@RequestBody PaymentRequest.Prepare prepareRequest) {
        redisService.saveDataWithExpiration(
            prepareRequest.getOrderId(), // key
            prepareRequest.getAmount().toString(), // value
            60 * 5 // 5분 TTL
        );
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.PAYMENT_PREPARED_SUCCESS));
    }

    @PostMapping("/confirm")
    @UserOnly
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequest.Confirm confirmRequest, @Auth Accessor accessor) throws IOException, InterruptedException {
        Long userId = accessor.userId();
        System.out.println("유저:" + userId);

        validatePaymentConfirmRequestV2(confirmRequest);
        Response<Object> response = paymentService.sendPaymentRequest(confirmRequest);
        //토스페이먼츠로 보낸 결제 승인 요청에 대한 response 리턴
        Response<Object> objectResponse = paymentService.handleTossPaymentResponse(userId, confirmRequest, response);

        if (objectResponse.isSuccess()) {
            PaymentResponse paymentResponse = (PaymentResponse) objectResponse.getBody();
            return ResponseEntity.ok(ApiResult.success("결제가 성공적으로 처리되었습니다.", paymentResponse));
        }
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    private void validatePaymentConfirmRequestV2(PaymentRequest.Confirm confirmRequest) {
        Optional<String> amountStr = getAmountStr(confirmRequest.getOrderId());

        BigDecimal savedAmount = amountStr
            .map(BigDecimal::new)
            .orElseThrow(() -> new RedisKeyNotFoundException("Redis key 'orderId' not found."));

        if (!confirmRequest.getAmount().equals(savedAmount)) {
            throw new PaymentDataMismatchException("Payment data mismatch : Amount does not match the previous value");
        }
    }

    private Optional<String> getAmountStr(String orderId) {
        return Optional.ofNullable(redisService.getValue(orderId));
    }
}

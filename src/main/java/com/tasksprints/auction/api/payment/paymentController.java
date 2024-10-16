package com.tasksprints.auction.api.payment;

import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/prepare")
    @Operation(summary = "Temporarily stores the payment element", description = "Save orderID and amount in session")
    @ApiResponse(responseCode = "200", description = "Payment prepared successfully")
    public ResponseEntity<ApiResult<String>> preparePayment(HttpSession session, @RequestBody PaymentRequest.Prepare prepareRequest) {
        paymentService.prepare(session, prepareRequest);
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.PAYMENT_PREPARED_SUCCESS));
    }


}

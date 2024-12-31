package com.tasksprints.auction.payment.presentation;

import com.tasksprints.auction.BaseControllerTest;
import com.tasksprints.auction.auth.application.resolver.AuthenticationResolver;
import com.tasksprints.auction.auth.domain.model.Accessor;
import com.tasksprints.auction.payment.api.Response;
import com.tasksprints.auction.payment.application.service.PaymentService;
import com.tasksprints.auction.payment.domain.dto.response.PaymentErrorResponse;
import com.tasksprints.auction.payment.domain.dto.response.PaymentResponse;
import com.tasksprints.auction.payment.exception.PaymentDataMismatchException;
import com.tasksprints.auction.payment.exception.RedisKeyNotFoundException;
import com.tasksprints.auction.payment.infrastructure.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.tasksprints.auction.common.constant.ApiResponseMessages.PAYMENT_PREPARED_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PaymentControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private RedisService redisService;

    @MockBean
    private AuthenticationResolver authResolver;
    private Accessor accessor;

    @BeforeEach
    void setup() throws Exception {
        accessor = Accessor.user(1L);
        when(authResolver.supportsParameter(any())).thenReturn(true);
        when(authResolver.resolveArgument(any(),any(),any(),any())).thenReturn(accessor);
    }

    @Test
    @DisplayName("결제 전 임시 값 저장")
    public void 결제_전_임시_값_저장() throws Exception {
        String jsonRequest = """
            {
                "orderId": "orderId",
                "amount": 1000.00
            }
            """;

        mockMvc.perform(post("/api/v1/payment/prepare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(PAYMENT_PREPARED_SUCCESS));

        verify(redisService).saveDataWithTimeOut(
            eq("orderId"),
            eq("1000.00"),
            eq(300L)
        );
    }

    @Nested
    class RedisTest {
        @Test
        void 결제_전_Redis_key_value값이_null인_경우_예외가_발생한다() throws Exception {
            // Given
            String jsonRequest = """
                {
                    "orderId": "orderId",
                    "amount": 10000
                }
                """;
            when(redisService.getValue(any(String.class))).thenReturn(null);

            // When & Then
            mockMvc.perform(post("/api/v1/payment/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                    .isInstanceOf(RedisKeyNotFoundException.class)
                    .hasMessage("Redis key 'orderId' not found."));

        }

        @Test
        @DisplayName("결제 전 Redis에 저장된 amount와 결제 요청 전 request의 amount가 다르면 예외가 발생한다")
        void 결제_amount가_결제_과정중_변경되면_예외가_발생한다() throws Exception {
            // Given
            String jsonRequest = """
                {
                    "orderId": "orderId",
                    "amount": 10000
                }
                """;
            when(redisService.getValue(any(String.class))).thenReturn("99999"); //changed-amount

            // When & Then
            mockMvc.perform(post("/api/v1/payment/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                    .isInstanceOf(PaymentDataMismatchException.class)
                    .hasMessage("Payment data mismatch : Amount does not match the previous value"));

        }
    }

    @Test
    @DisplayName("결제 승인 성공 시 HTTP 200 응답을 반환한다")
    void 결제_승인_성공_시_응답() throws Exception {
        // Given
        String jsonRequest = """
            {
                "orderId": "orderId",
                "amount": 10000
            }
            """;

        PaymentResponse successPaymentResponse = new PaymentResponse("CARD", "paymentKey", BigDecimal.valueOf(10000), "Test Order", "orderId", "DONE");
        Response<Object> mockResponse = Response.success(200, successPaymentResponse);

        when(redisService.getValue("orderId")).thenReturn("10000");
        when(paymentService.sendPaymentRequest(any())).thenReturn(mockResponse);
        when(paymentService.handleTossPaymentResponse(anyLong(), any(), any()))
            .thenReturn(mockResponse);

        // When / Then
        mockMvc.perform(post("/api/v1/payment/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("결제가 성공적으로 처리되었습니다."))
            .andExpect(jsonPath("$.data.orderId").value("orderId"))
            .andExpect(jsonPath("$.data.totalAmount").value(10000));
    }

    @Test
    @DisplayName("결제 승인 실패 시 HTTP 400 응답을 반환한다")
    void 결제_승인_실패_시_응답() throws Exception {
        // Given
        String jsonRequest = """
            {
                "orderId": "orderId",
                "amount": 10000
            }
            """;

        PaymentErrorResponse failurePaymentResponse = PaymentErrorResponse.builder()
            .version("2022-11-16")
            .traceId("{traceId}")
            .code("{CODE}")
            .message("{MESSAGE}")
            .build();
        Response<Object> mockResponse = Response.failure(400, failurePaymentResponse);

        when(redisService.getValue("orderId")).thenReturn("10000");
        when(paymentService.sendPaymentRequest(any())).thenReturn(mockResponse);
        when(paymentService.handleTossPaymentResponse(anyLong(), any(), any()))
            .thenReturn(mockResponse);

        // When / Then
        mockMvc.perform(post("/api/v1/payment/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("{CODE}"))
            .andExpect(jsonPath("$.message").value("{MESSAGE}"));
    }

}

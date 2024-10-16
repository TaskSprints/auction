package com.tasksprints.auction.domain.payment.service;

import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest) {
        session.setAttribute("orderId", prepareRequest.getOrderId());
        session.setAttribute("amount", prepareRequest.getAmount());
    }
}

package com.tasksprints.auction.domain.payment.service;


import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import jakarta.servlet.http.HttpSession;

public interface PaymentService {
    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest);
}

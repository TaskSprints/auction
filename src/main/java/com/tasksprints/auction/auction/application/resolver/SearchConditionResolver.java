package com.tasksprints.auction.auction.application.resolver;

import com.tasksprints.auction.auction.domain.dto.request.AuctionRequest;
import com.tasksprints.auction.auction.domain.entity.AuctionCategory;
import com.tasksprints.auction.auction.domain.entity.AuctionStatus;
import com.tasksprints.auction.product.domain.entity.ProductCategory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SearchConditionResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 메서드 파라미터 타입이 AuctionRequest.SearchCondition일 때 처리
        return parameter.getParameterType().equals(AuctionRequest.SearchCondition.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // QueryString에서 값을 추출
        String auctionCategory = webRequest.getParameter("auctionCategory");
        String productCategory = webRequest.getParameter("productCategory");
        String startTime = webRequest.getParameter("startTime");
        String endTime = webRequest.getParameter("endTime");
        String minPrice = webRequest.getParameter("minPrice");
        String maxPrice = webRequest.getParameter("maxPrice");
        String auctionStatus = webRequest.getParameter("auctionStatus");
        String sortBy = webRequest.getParameter("sortBy");
        // 파싱 및 변환
        AuctionCategory parsedAuctionCategory = auctionCategory != null ? AuctionCategory.fromDisplayName(auctionCategory) : null;
        ProductCategory parsedProductCategory = productCategory != null ? ProductCategory.fromDisplayName(productCategory) : null;
        LocalDateTime parsedStartTime = startTime != null ? LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME) : null;
        LocalDateTime parsedEndTime = endTime != null ? LocalDateTime.parse(endTime, DateTimeFormatter.ISO_DATE_TIME) : null;
        BigDecimal parsedMinPrice = minPrice != null ? new BigDecimal(minPrice) : null;
        BigDecimal parsedMaxPrice = maxPrice != null ? new BigDecimal(maxPrice) : null;
        AuctionStatus parsedAuctionStatus = auctionStatus != null ? AuctionStatus.fromDisplayName(auctionStatus) : null;

        //값 검증(비즈니스 로직이 아닌 값 유효성 검증은 resolver가 담당)
        validateBothTimesProvided(parsedStartTime, parsedEndTime);
        validateIsStartBeforeEnd(parsedStartTime, parsedEndTime);
        validateMinLessThanMax(parsedMinPrice, parsedMaxPrice);


        // SearchCondition 객체 생성 및 반환
        return new AuctionRequest.SearchCondition(
            parsedAuctionCategory,
            parsedProductCategory,
            parsedStartTime,
            parsedEndTime,
            parsedMinPrice,
            parsedMaxPrice,
            parsedAuctionStatus,
            sortBy
        );
    }

    private void validateMinLessThanMax(BigDecimal parsedMinPrice, BigDecimal parsedMaxPrice) {
        if (parsedMinPrice != null && parsedMaxPrice != null) {
            if (parsedMinPrice.compareTo(parsedMaxPrice) > 0) {
                throw new IllegalArgumentException("minPrice cannot be greater than maxPrice.");
            }
        }
    }

    private void validateIsStartBeforeEnd(LocalDateTime parsedStartTime, LocalDateTime parsedEndTime) {
        if (parsedStartTime != null && parsedEndTime != null) {
            if (parsedStartTime.isAfter(parsedEndTime)) {
                throw new IllegalArgumentException("startTime cannot be after endTime.");
            }
        }
    }

    private void validateBothTimesProvided(LocalDateTime parsedStartTime, LocalDateTime parsedEndTime) {
        if ((parsedStartTime == null && parsedEndTime != null) || (parsedStartTime != null && parsedEndTime == null)) {
            throw new IllegalArgumentException("Both startTime and endTime must be provided together.");
        }
    }
}

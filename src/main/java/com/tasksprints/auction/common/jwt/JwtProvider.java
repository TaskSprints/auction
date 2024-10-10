package com.tasksprints.auction.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    /**
     * accessToken 을 생성합니다.
     * */
    public String createAccessToken(Long userId) {

        String accessToken = Jwts.builder()
            .setIssuer(jwtProperties.getIssuer())
            .claim("userId", userId)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpireMs()))
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey().getBytes())
            .compact();

        return accessToken;
    }
}

package com.tasksprints.auction.common.jwt;

import io.jsonwebtoken.Claims;
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
     * TODO: createToken 으로 변경 -> refreshToken 확장성 고려
     * accessToken 을 생성합니다.
     * */
    public String createAccessToken(Long userId) {

        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
            .setIssuer(jwtProperties.getIssuer())
            .claim("userId", userId)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + jwtProperties.getExpireMs()))
            .signWith(SignatureAlgorithm.HS256, JwtUtil.encodeSecretKey(jwtProperties.getSecretKey()))
            .compact();
    }

    /**
     * refresh token 을 생성합니다.
     * */
    public String createRefreshToken() {

        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + jwtProperties.getRefreshExpireMs()))
            .signWith(SignatureAlgorithm.HS256, JwtUtil.encodeSecretKey(jwtProperties.getSecretKey()))
            .compact();
    }

    /**
     * 토큰이 유효한지 검증합니다.
     * */
    public boolean verifyToken(String token) {

        Date now = new Date(System.currentTimeMillis());

        Claims claims = getClaims(token);

        return !claims.getExpiration().before(now);
    }

    /**
     * 토큰에서 추출한 정보를 반환합니다.
     * */
    public Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(JwtUtil.encodeSecretKey(jwtProperties.getSecretKey()))
            .parseClaimsJws(token)
            .getBody();
    }


    /**
     * TODO:
     *  1. Claims 리팩토링
     *  2. 토큰 종류에 따른 expire 시간 다른 반환
     *      - claim 길이 유무로 판단
     * */
}

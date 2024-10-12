package com.tasksprints.auction.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private JwtProperties jwtProperties;
    @InjectMocks
    private JwtProvider jwtProvider;

    private final Long validExpireMs = 36000000L;
    private final Long expiredExpireMs = 0L;
    private final String issuer = "testIssuer";
    private final String secretKey = "testSecretKey";


    @BeforeEach
    public void setUp() {
        when(jwtProperties.getIssuer()).thenReturn(issuer);
        when(jwtProperties.getSecretKey()).thenReturn(secretKey);
    }

    private void stubExpiration(Long expireMs) {
        when(jwtProperties.getExpireMs()).thenReturn(expireMs);
    }

    @Test
    @DisplayName("access token 발급 테스트")
    void createAccessToken() {
        stubExpiration(validExpireMs);

        String token = jwtProvider.createAccessToken(1L);

        assertNotNull(token, "access token 이 발급되어야 합니다.");
    }

    @Test
    @DisplayName("유효한 토큰 테스트")
    void verifyToken_valid() {
        stubExpiration(validExpireMs);

        String token = jwtProvider.createAccessToken(1L);

        Assertions.assertTrue(jwtProvider.verifyToken(token));
    }

    @Test
    @DisplayName("만료된 토큰 테스트")
    void verifyToken_expired() {
        stubExpiration(expiredExpireMs);

        String token = jwtProvider.createAccessToken(1L);

        Assertions.assertThrows(ExpiredJwtException.class,
            () -> { jwtProvider.verifyToken(token); },
            "토큰이 즉시 만료되어야 합니다.");
    }

    @Test
    @DisplayName("디코딩 된 페이로드 정확성 테스트")
    void getClaims() {
        stubExpiration(validExpireMs);

        String token = jwtProvider.createAccessToken(1L);
        Long decodedUserId = jwtProvider.getClaims(token).get("userId", Long.class);

        assertThat(decodedUserId).isEqualTo(1L);
    }
}

package com.tasksprints.auction.common.jwt;

import com.tasksprints.auction.common.jwt.dto.response.JwtResponse;
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

    private final Long VALID_EXPIRE_MS = 36000000L;
    private final Long REFRESH_EXPIRE_MS = 72000000L;
    private final Long EXPIRED_EXPIRE_MS = 0L;
    private final String ISSUER = "testIssuer";
    private final String SECRET_KEY = "testSecretKey";


    @BeforeEach
    public void setUp() {
        when(jwtProperties.getIssuer()).thenReturn(ISSUER);
        when(jwtProperties.getSecretKey()).thenReturn(SECRET_KEY);
    }

    private void stubAccessTokenExpiration(Long expireMs) {
        when(jwtProperties.getExpireMs()).thenReturn(expireMs);
    }

    private void stubRefreshTokenExpiration(Long expireMs) {
        when(jwtProperties.getRefreshExpireMs()).thenReturn(expireMs);
    }

    @Test
    @DisplayName("token generator 을 통한 access token, refresh token 발급 테스트")
    void generateToken() {
        JwtResponse jwtResponse = jwtProvider.generateToken(1L, "admin");

        assertNotNull(jwtResponse.getAccessToken(), "access token 이 발급되어야 합니다.");
        assertNotNull(jwtResponse.getRefreshToken(), "refresh token 이 발급되어야 합니다.");
    }

    @Test
    @DisplayName("access token 발급 테스트")
    void createAccessToken() {
        stubAccessTokenExpiration(VALID_EXPIRE_MS);

        String token = jwtProvider.createAccessToken(1L, "admin");

        assertNotNull(token, "access token 이 발급되어야 합니다.");
    }

    @Test
    @DisplayName("refresh token 발급 테스트")
    void createRefreshToken() {
        stubRefreshTokenExpiration(REFRESH_EXPIRE_MS);
        String token = jwtProvider.createRefreshToken();
        assertNotNull(token, "refresh token 이 발급되어야 합니다.");
    }

    @Test
    @DisplayName("유효한 토큰 테스트")
    void verifyToken_valid() {
        stubAccessTokenExpiration(VALID_EXPIRE_MS);

        String token = jwtProvider.createAccessToken(1L, "admin");

        Assertions.assertTrue(jwtProvider.verifyToken(token));
    }

    @Test
    @DisplayName("만료된 토큰 테스트")
    void verifyToken_expired() {
        stubAccessTokenExpiration(EXPIRED_EXPIRE_MS);

        String token = jwtProvider.createAccessToken(1L, "admin");

        Assertions.assertThrows(ExpiredJwtException.class,
            () -> { jwtProvider.verifyToken(token); },
            "토큰이 즉시 만료되어야 합니다.");
    }

    @Test
    @DisplayName("디코딩 된 페이로드 정확성 테스트")
    void getClaims() {
        stubAccessTokenExpiration(VALID_EXPIRE_MS);

        String token = jwtProvider.createAccessToken(1L, "admin");
        Long decodedUserId = jwtProvider.getClaims(token).get("userId", Long.class);
        String decodedUserRole = jwtProvider.getClaims(token).get("userRole", String.class);

        assertThat(decodedUserId).isEqualTo(1L);
        assertThat(decodedUserRole).isEqualTo("admin");
    }
}

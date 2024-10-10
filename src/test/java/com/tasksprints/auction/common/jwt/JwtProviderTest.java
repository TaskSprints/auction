package com.tasksprints.auction.common.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private JwtProperties jwtProperties;
    @InjectMocks
    private JwtProvider jwtProvider;
    @BeforeEach
    public void setUp() {
        when(jwtProperties.getIssuer()).thenReturn("testIssuer");
        when(jwtProperties.getSecretKey()).thenReturn("testSecret");
        when(jwtProperties.getExpireMs()).thenReturn(3600000L);
    }

    @Test
    @DisplayName("access token 발급 테스트")
    void createAccessToken() {
        String token = jwtProvider.createAccessToken(1L);
        assertNotNull(token, "토큰이 발급되었습니다.");
    }
}
ㅎ

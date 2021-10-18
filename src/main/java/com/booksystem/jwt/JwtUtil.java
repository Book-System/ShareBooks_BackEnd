package com.booksystem.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
    private final String SECRETKEY = "2asdjklajdlas";

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(String memberid) {
        long tokenValidTime = 1000 * 60 * 60 * 24;

        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder().setClaims(claims).setSubject(memberid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();

        return token;
    }

    // 토큰에서 해당 아이디 정보 가져오기
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 만료시간 가져오기
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰의 만료시간이 유효한지 확인
    public Boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    // 토큰 유효성 확인
    public Boolean validateToken(String token, String memberid) {
        final String username = this.extractUsername(token);

        if (username.equals(memberid) && !isTokenExpired(token)) {
            return true;
        }
        return false;
    }
}

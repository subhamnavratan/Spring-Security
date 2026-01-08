package com.Subham.PRASAG.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    /*
     🔒 JJWT-generated secure key
     - Guaranteed ≥ 256 bits
     - No Base64
     - No config issues
    */
    private final Key signingKey =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /*Extracts username (email) from JWT*/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*Generic method to extract any claim*/
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        return resolver.apply(extractAllClaims(token));
    }

    /*
      A JWT has 3 parts:
      HEADER . PAYLOAD . SIGNATURE
      Claims live in the PAYLOAD
    */
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*Generate JWT token*/
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "roles",
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtExpiration)
                )
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /*Validate token*/
    public boolean isTokenValid(String token, UserDetails userDetails) {

        return extractUsername(token)
                .equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /*Check token expiration*/
    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }
}

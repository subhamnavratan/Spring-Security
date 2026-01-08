package com.Subham.PRASAG.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
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

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);/*getSubject()->Reads the sub claim   Subject usually = username / email*/
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver/*claims is input and T is output*/)
    {
        return resolver.apply(extractAllClaims(token));
        /*“Take all claims from the token, and apply the resolver function to get the required value.”*/
    }
/*A JWT has 3 parts:
HEADER . PAYLOAD . SIGNATURE
Claims live in the PAYLOAD*/
    private Claims extractAllClaims(String token) {
        /*What is parserBuilder()?
          It creates a JWT parser builder.
          Think of it like:
          “I want to read a JWT, but first I need to tell HOW to read it.”*/
        return Jwts.parserBuilder()
                /*parserBuilder() creates a configurable JWT parser that
                 you can customize before actually parsing (verifying + reading) a token.*/
                .setSigningKey(getSignInKey())
                /*build() means:
                  “Finish configuration and create a ready-to-use parser object.”*/
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        /*claims = {"roles": ["ADMIN_READ", "ADMIN_WRITE"]}*/
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
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}

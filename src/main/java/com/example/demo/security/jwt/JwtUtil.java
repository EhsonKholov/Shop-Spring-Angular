package com.example.demo.security.jwt;

import com.example.demo.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtUtil {

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expired}")
    private long milliseconds;
    @Value("${jwt.token.refreshExpirationDateInMs}")
    private long millisecondsRefresh;


    public String createToken(String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));
        claims.put("token_type", "access_token");

        Date now = new Date();
        Date validity = new Date(now.getTime() + milliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String createRefreshToken(String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));
        claims.put("token_type", "refresh_token");

        Date now = new Date();
        Date validity = new Date(now.getTime() + millisecondsRefresh);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            String token_type = claims.getBody().get("token_type").toString();

            if (!token_type.equals("refresh_token")) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken);
            return claims.getBody().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getRoleNames(Set<Role> roles) {
        return roles.stream().map(r -> r.getName()).collect(Collectors.toList());
    }

}

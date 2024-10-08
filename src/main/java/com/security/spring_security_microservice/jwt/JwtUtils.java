package com.security.spring_security_microservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.security.Key;

@Component
public class JwtUtils {
    private static  final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private  String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header : {}", bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ") ){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder().
                subject(username).
                issuedAt(new Date()).
                expiration(new Date(new Date().getTime() + jwtExpirationMs )).
                signWith(key())
                .compact();
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}

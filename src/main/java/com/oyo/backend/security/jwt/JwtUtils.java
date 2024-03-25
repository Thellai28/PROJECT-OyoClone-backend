package com.oyo.backend.security.jwt;

import com.oyo.backend.security.user.HotelUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${security.jwt.secret")
    private String jwtSecret;

    @Value("${security.jwt.expirationTime")
    private int jwtExpirationDate;


    public String generateJwtTokenForUser( Authentication authentication ){
        HotelUserDetails userPrincipal = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Generally the user or the entity to be authenticated
                .claim("roles",roles ) // Custom claim, we are adding roles of the current user:
                .setIssuedAt( new Date()) // setting issued date
                .setExpiration( new Date( (new Date()).getTime() + jwtExpirationDate) ) // Time of creation + one hour
                .signWith( key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromToken( String token ){
        return Jwts.parserBuilder()
                .setSigningKey(key())// sets the securityKey
                .build()// building the parser object :
                .parseClaimsJwt(token)// getting the JWT object, which contains payload, header & signature.
                .getBody()// Getting the body section from the JWT object generated at previous step :
                .getSubject(); // The subject contains the name of the user or the entity :
    }

    public boolean validateToken( String token ){
        try{
            Jwts.parserBuilder().setSigningKey( key() ).build().parse(token);
            return true;
        }catch( MalformedJwtException ex ){
            logger.error( "Invalid JWT token {}", ex.getMessage());
        }catch( ExpiredJwtException ex ){
            logger.error( "Expired token : {}", ex.getMessage());
        }catch( UnsupportedJwtException ex ){
            logger.error("This token is not supported : {}", ex.getMessage());
        }catch ( IllegalArgumentException ex ){
            logger.error("This token is not supported : {}", ex.getMessage());
        }

        return false;
    }

}

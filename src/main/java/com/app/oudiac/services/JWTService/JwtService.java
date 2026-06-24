package com.app.oudiac.services.JWTService;

import com.app.oudiac.models.Admin;
import com.app.oudiac.models.User;
import com.app.oudiac.utils.ConstantUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private ObjectMapper objectMapper;

    public String generateJwtToken(User user){
        Map<String,Object> payload = new HashMap<>();
        payload.put("userId",user.getId());
        payload.put("iss","Oudiac");
        payload.put("userName",user.getName());
        payload.put("scope",user.getRole());
        payload.put("email",user.getEmail());
        payload.put("mob",user.getMobileNumber());
        payload.put("role",user.getRole());

        return Jwts.builder().claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ConstantUtils.JWT_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey).compact();
    }
    public Boolean validateToken(String token) {

        try{
            Claims claims =getClaims(token);

            Date expiration = claims.getExpiration();

            if (expiration.before(new Date())) {
                throw new InvalidOneTimeTokenException("Token has expired!!");
            }

//            System.out.println(tokenExpiry);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public String generateJwtTokenForAdmin(Admin user){
        Map<String,Object> payload = new HashMap<>();
        payload.put("userId",user.getId());
        payload.put("iss","Oudiac");
        payload.put("userName",user.getName());
        payload.put("scope",user.getRole());
        payload.put("email",user.getEmail());
        payload.put("mob",user.getMobileNumber());
        payload.put("role",user.getRole());

        return Jwts.builder().claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ConstantUtils.JWT_TOKEN_EXPIRATION_TIME_ADMIN))
                .signWith(secretKey).compact();
    }

    public Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        return jwtParser.parseSignedClaims(token).getPayload();
    }
}

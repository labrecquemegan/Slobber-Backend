package com.project.slobber.auth;

import com.project.slobber.auth.dtos.response.Principal;
import com.project.slobber.user.User;
import com.project.slobber.user.UserService;
import com.project.slobber.util.annotations.Inject;
import com.project.slobber.util.custom_exception.InvalidRequestException;
import com.project.slobber.util.custom_exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class TokenService {

    @Inject
    private JwtConfig jwtConfig;
    private UserService userService;

    public TokenService(){}

    @Inject
    @Autowired
    public TokenService(JwtConfig jwtConfig, UserService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    public String generateToken(Principal principal) {
        long now = System.currentTimeMillis();
        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(principal.getId())
                .setIssuer("Slobber")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration()))
                .setSubject(principal.getUsername())
                .claim("role", principal.getRole())
                .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return tokenBuilder.compact();
    }

    public Principal extractRequesterDetails(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

            return new Principal(claims.getId(), claims.getSubject(), claims.get("role", String.class));
        } catch (Exception e) {
            return null;
        }
    }

    public Principal noTokenThrow(String token){
        Principal requester = extractRequesterDetails(token);
        if(requester == null) throw new UnauthorizedException("No authorization found");//401
        User user = userService.getUserByUsername(requester.getUsername());
        if(user == null) throw new InvalidRequestException("Invalid user token");//404
        //if(!user.getIsActive()) throw new AuthenticationException("Inactive user token");//403
        return requester;
    }
}

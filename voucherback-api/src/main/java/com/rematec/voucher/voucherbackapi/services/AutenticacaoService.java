package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.security.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AutenticacaoService {

    private static final String BEARER = "Bearer ";
    private static final String HEAD_AUTHORIZATION = "Authorization";
    private static final String JWT_KEY = "signinKey";
    private static final String AUTHORITIES = "authorities";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private static final int EXPIRATION_TOKEN = 3600000;

    static public void addJWTToken(HttpServletResponse response, Authentication authentication) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES, authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));


        String jwtToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .claim("nome",((UserDetail) authentication.getPrincipal()).getNome() )
                .addClaims(claims)
                .compact();

        response.addHeader(HEAD_AUTHORIZATION, BEARER + jwtToken);
        response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEAD_AUTHORIZATION);
    }

    static public Authentication obterAutenticacao(HttpServletRequest request) {

        String token = request.getHeader(HEAD_AUTHORIZATION);
        if (token != null) {
            Claims user = Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token.replace(BEARER, ""))
                    .getBody();

            if (user != null) {

                List<SimpleGrantedAuthority> permissoes = ((ArrayList<String>)user.get(AUTHORITIES))
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                return new UsernamePasswordAuthenticationToken(user, null, permissoes);
            } else {
                throw new RuntimeException("Autenticação falhou.");
            }

        }
        return null;
    }

}

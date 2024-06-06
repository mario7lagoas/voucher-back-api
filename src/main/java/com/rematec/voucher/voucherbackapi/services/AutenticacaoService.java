package com.rematec.voucher.voucherbackapi.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rematec.voucher.voucherbackapi.security.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
public class AutenticacaoService {

    private static final String BEARER = "Bearer ";
    private static final String HEAD_AUTHORIZATION = "Authorization";
    private static final String AUTHORITIES = "authorities";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private static final String REFRESH_TOKEN = "RefreshToken";

    private static String JWT_KEY ;
    private static int EXPIRATION_TOKEN;
    private static int EXPIRATION_REFRESH_TOKEN;
    @Value("${jwt.secret}")
    private void setKey(String key){
        JWT_KEY = key;
    }
    @Value("${jwt.expiration}")
    private void setExpiration(int expiration){
        EXPIRATION_TOKEN = expiration;
    }

    @Value("${jwt.refreshToken}")
    private void setRefresh(int refresh){
        EXPIRATION_REFRESH_TOKEN = refresh;
    }

    static public void addJWTToken(HttpServletResponse response, HttpServletRequest request,
                                   Authentication authentication) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES, authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String jwtToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .setIssuer(request.getRequestURL().toString())
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .claim("nome", ((UserDetail) authentication.getPrincipal()).getNome())
                .claim("empresa", ((UserDetail) authentication.getPrincipal()).getEmpresa())
                .addClaims(claims)
                .compact();

        String jwtReshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + 10L * EXPIRATION_REFRESH_TOKEN))
                .setIssuer(request.getRequestURL().toString())
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .compact();

        Cookie cookie = new Cookie(REFRESH_TOKEN, jwtReshToken);
        cookie.setMaxAge(EXPIRATION_REFRESH_TOKEN);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath(request.getContextPath() + "/login/refresh");

        response.addCookie(cookie);

        response.addHeader(HEAD_AUTHORIZATION, BEARER + jwtToken);

        response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEAD_AUTHORIZATION);
    }

    static public Authentication obterAutenticacao(HttpServletRequest request,
                                                   HttpServletResponse response) {

        String token = request.getHeader(HEAD_AUTHORIZATION);

            if (token != null) {
                Claims user = null;
               try {
                   user = Jwts.parser()
                           .setSigningKey(JWT_KEY)
                           .parseClaimsJws(token.replace(BEARER, ""))
                           .getBody();

               }catch (ExpiredJwtException e){
                   try {
                       response.setHeader("error", e.getMessage());
                       response.setStatus(FORBIDDEN.value());
                       Map<String, String> error = new HashMap<>();
                       error.put("codigo", "JWT_ERRO");
                       error.put("mensagem", "Token Expirado.");
                       response.setContentType(APPLICATION_JSON_VALUE);
                       new ObjectMapper().writeValue(response.getOutputStream(), error);

                   } catch (IOException ex) {
                       throw new RuntimeException(ex);
                   }

               }

                if (user != null && user.get(AUTHORITIES) != null) {

                    List<SimpleGrantedAuthority> permissoes = ((ArrayList<String>) user.get(AUTHORITIES))
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    return new UsernamePasswordAuthenticationToken(user, null, permissoes);
                } else {

                    try {
                        response.setHeader("error", "Autenticação falhou.");
                        response.setStatus(FORBIDDEN.value());
                        Map<String, String> error = new HashMap<>();
                        error.put("codigo", "JWT_ERRO");
                        error.put("mensagem", "Autenticação falhou.");
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), error);

                    } catch (IOException e) {
                        throw new RuntimeException("Autenticação falhou. " + e.getMessage());
                    }
                }
            }

        return null;
    }

}
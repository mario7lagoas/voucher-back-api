package com.rematec.voucher.voucherbackapi.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.security.UserDetail;
import com.rematec.voucher.voucherbackapi.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
public class AutenticacaoService {

    private static final String BEARER = "Bearer ";
    private static final String HEAD_AUTHORIZATION = "Authorization";

    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String JWT_KEY = "signinKey";

    private static final String AUTHORITIES = "authorities";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    //private static final int EXPIRATION_TOKEN = 3600000;
    private static final int EXPIRATION_TOKEN = 2* 900000;
    private static final int EXPIRATION_REFRESH_TOKEN = 3600000;

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


        // response.addHeader(REFRESH_TOKEN, jwtReshToken);
/*
        Map<String,String> tokens = new HashMap<>();

        tokens.put(HEAD_AUTHORIZATION, BEARER + jwtToken);
        tokens.put(REFRESH_TOKEN, jwtReshToken);

       response.setContentType(APPLICATION_JSON_VALUE );
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */

        response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEAD_AUTHORIZATION);
        // response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, REFRESH_TOKEN);
/*
        //Retorno token no corpo
        try {
            response.getWriter().write(jwtToken);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

 */
    }

    static public Authentication obterAutenticacao(HttpServletRequest request,
                                                   HttpServletResponse response) {

        String token = request.getHeader(HEAD_AUTHORIZATION);

        System.out.println(token);
        try {

            if (token != null) {


                Claims user = Jwts.parser()
                        .setSigningKey(JWT_KEY)
                        .parseClaimsJws(token.replace(BEARER, ""))
                        .getBody();


                if (user != null && user.get(AUTHORITIES) != null) {

                    List<SimpleGrantedAuthority> permissoes = ((ArrayList<String>) user.get(AUTHORITIES))
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    return new UsernamePasswordAuthenticationToken(user, null, permissoes);
                } else {
                    throw new RuntimeException("Autenticação falhou.");
                }


            }
        } catch (Exception ex) {

            try {

                log.error("Error em realizar no login: {}", ex.getMessage());
                response.setHeader("error", ex.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("codigo", "JWT_ERRO");
                error.put("mensagem", ex.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        log.error("Token não informado.");
        response.setHeader("error", "Token não informado");
        response.setStatus(UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("codigo", "JWT_ERRO");
        error.put("mensagem", "Token não informado");
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}

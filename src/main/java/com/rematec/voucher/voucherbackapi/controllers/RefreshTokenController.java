package com.rematec.voucher.voucherbackapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioInativoException;
import com.rematec.voucher.voucherbackapi.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;

import com.rematec.voucher.voucherbackapi.security.UserDetail;
import com.rematec.voucher.voucherbackapi.utils.JWTUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/login")
public class RefreshTokenController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String AUTHORITIES = "authorities";
    private static final String HEAD_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

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



    @PostMapping("/refresh")
    public void refreshtoken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;

        if ("/voucher-back/v1/login/refresh".equalsIgnoreCase(request.getRequestURI())
                && "refresh_token".equals(request.getParameter("grant_type"))
                && request.getCookies() != null) {

            boolean checkCookieAdd = false;

            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("RefreshToken") && checkCookieAdd == false) {
                    refreshToken = cookie.getValue();
                    checkCookieAdd = true;
                }
            }

            if (refreshToken != null) {
                try {

                    if (jwtUtil.tokenValido(refreshToken)) {

                        String email = jwtUtil.getUsername(refreshToken);

                        UsuarioEntity user = iUsuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " não Encontrado!"));

                        if (user.getStatus().compareTo(false) == 0){
                            throw new UsuarioInativoException("Usuário "+ user.getUserName() + " Inativado!");
                        }

                        Map<String, Object> claims = new HashMap<>();

                        claims.put(AUTHORITIES, jwtUtil.getPrefilRolesPerfilUsuario(user));
                        claims.put("nome", user.getUserName());

                        String jwtToken = Jwts.builder()
                                .setSubject(user.getEmail())
                                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                                .setIssuer(request.getRequestURL().toString())
                                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                                .claim("empresa", user.getEmpresa() != null? user.getEmpresa().getNome() : "VOUCHER")
                                .claim("empresaGuid", user.getEmpresa() != null ? user.getEmpresa().getGuid() : "")
                                .addClaims(claims)
                                .compact();

                        String jwtReshToken = Jwts.builder()
                                .setSubject(user.getEmail())
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


                    } else {
                        throw new RuntimeException("Token inválido");
                    }

                } catch (Exception ex) {
                    try {
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
            }

        }

    }

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest req, HttpServletResponse resp) {

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath(req.getContextPath() + "/login/refresh");
        cookie.setMaxAge(0);

        resp.addCookie(cookie);
        resp.setStatus(HttpStatus.NO_CONTENT.value());

    }

}

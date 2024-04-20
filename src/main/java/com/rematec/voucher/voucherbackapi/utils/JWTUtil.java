package com.rematec.voucher.voucherbackapi.utils;

import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
@Getter
@ToString
public class JWTUtil {

    @Value("${security.jwt.secret:signinKey}")
    private String secret;

    @Value("${security.jwt.expiration:3600000}")
    private Long expirationToken;

    @Value("${security.jwt.expiration:259200000}")
    private Long expirationRefreshToken;

    @Value("${security.jwt.uri:/login}")
    private String uri;

    @Value("${security.jwt.header-refresh-token:refresh_token}")
    private String refreshToken;

    @Value("${security.jwt.header:Authorization}")
    private String headerAuthorization;

    @Value("${security.jwt.prefix:Bearer }")
    private String bearer;

    @Value("${security.jwt.prefix:authorities}")
    private String authorities;

    @Value("${security.jwt.access-control-exposeHeaders:Access-Control-Expose-Headers}")
    private String accessControlExposeHeaders;



    public boolean tokenValido(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {

            String username = claims.getSubject(); // Pega o usuário
            Date expirationDate = claims.getExpiration(); // Data de Expiração
            Date now = new Date(System.currentTimeMillis()); // Data Atual
            if (username != null && expirationDate != null && now.before(expirationDate)) { // Validando o token
                return true;
            }
        }
        return false;
    }

    private Claims getClaims(String token) { // Claims = Reivindicações do token

        try {
            return Jwts.parser()
                    .setSigningKey("signinKey")
                    .parseClaimsJws(token)
                    .getBody(); // Recupera os claims de agordo com o token

        } catch (Exception e) {
            return null;
        }
    }

    public String getUsername(String token) { // Retorna o usuario
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) { // Retorna authorities do usuario
        Claims claims = getClaims(token);
        if (claims != null && claims.get("authorities") != null) {
            String s = claims.get("authorities")
                    .toString()
                    .replace("[", "")
                    .replace("]","");

            return Arrays.stream(s.split(",")).map(SimpleGrantedAuthority::new).toList();
        }

        return null;
    }

    public List<String> getPrefilRolesPerfilUsuario(UsuarioEntity usuario) {

        List<String> novasRoles = new ArrayList();
        novasRoles.clear();

        List<List<String>> pefilsRoles = usuario.getPerfis()
                .stream()
                .map(pefilRoles -> pefilRoles.getRoles()
                        .stream()
                        .map(role -> role.getNome().getRole())
                        .toList()
                )
                .toList();

        pefilsRoles.forEach(listRoles -> {
            listRoles.forEach(role -> {
                if (!novasRoles.contains(role))
                    novasRoles.add(role);
            });
        });

        return novasRoles;
    }


}

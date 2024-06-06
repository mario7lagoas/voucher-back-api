package com.rematec.voucher.voucherbackapi.security;

import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetail implements UserDetails {

    private String nome;
    private String email;
    private String password;
    private EmpresaEntity empresa;
    private Collection<? extends GrantedAuthority> authorities; //perfis

    public UserDetail(String nome, String email, String password, EmpresaEntity empresa, List<String> profiles) {
        super();
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.empresa = empresa;
        this.authorities = profiles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Implementa aqui a regra de negocio para conta Expirada
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Implementa aqui a regra de negocio para conta Bloqueada
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Implementa aqui a regra de negocio para credencias expirada
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Implementa aqui a regra de negocio para verificar se usuario ativo
        return true;
    }
}

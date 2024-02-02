package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.security.UserDetail;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class UsuarioAutenticadoService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuarioEntity usuario = iUsuarioRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email " + email + " não Encontrado!"));

        List<String> roles = usuario.getRoles()
                .stream()
                .map(role -> role.getNome().getRole())
                .collect(Collectors.toList());
        return new UserDetail(usuario.getUserName(),usuario.getEmail(), usuario.getPassword(), roles);
    }
}

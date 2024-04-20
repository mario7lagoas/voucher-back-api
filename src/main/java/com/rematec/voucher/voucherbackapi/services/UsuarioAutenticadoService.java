package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.UsuarioInativoException;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.security.UserDetail;
import com.rematec.voucher.voucherbackapi.utils.JWTUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioAutenticadoService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuarioEntity usuario = iUsuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " não Encontrado!"));

        if (usuario.getStatus().compareTo(false) == 0){
            throw new UsuarioInativoException("Usuário "+ usuario.getUserName() + " Inativado!");
        }

        return new UserDetail(usuario.getUserName(), usuario.getEmail(), usuario.getPassword(), jwtUtil.getPrefilRolesPerfilUsuario(usuario));
    }

}

package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.UsuarioInativoException;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.security.UserDetail;
import com.rematec.voucher.voucherbackapi.utils.JWTUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioAutenticadoService implements UserDetailsService {

    private final IUsuarioRepository iUsuarioRepository;

    private final JWTUtil jwtUtil;

    public UsuarioAutenticadoService(final IUsuarioRepository iUsuarioRepository, final JWTUtil jwtUtil) {
        this.iUsuarioRepository = iUsuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuarioEntity usuario = iUsuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " não Encontrado!"));

        if (usuario.getStatus().compareTo(false) == 0) {
            throw new UsuarioInativoException("Usuário " + usuario.getUserName() + " Inativado!");
        }

        return new UserDetail(usuario.getUserName(), usuario.getEmail(), usuario.getPassword(), usuario.getEmpresa(),
                jwtUtil.getPrefilRolesPerfilUsuario(usuario));
    }

}

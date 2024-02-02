package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.UsuarioNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IRoleRepository;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioRequest;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioCadastroResponse;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleRepository iRoleRepository;

    public List<UsuarioCadastroResponse> gelAll() {

        List<UsuarioEntity> entities = iUsuarioRepository.findAll();
        return mapper.listUsuarioEntityTolistUsuarioResponse(entities);
    }

    public UsuarioCadastroResponse addUsuario(UsuarioRequest usuarioRequest) {
        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .guid(UUID.randomUUID().toString())
                .userName(usuarioRequest.getUserName())
                .email(usuarioRequest.getEmail())
                .password(passwordEncoder.encode(usuarioRequest.getPassword()))
                .roles(addRoles(usuarioRequest.getRoles()))
                .build();

        return mapper.usuarioEntityToUsuarioCadastroResponse(iUsuarioRepository.save(usuarioEntity));
    }

    private List<RoleEntity> addRoles(List<RoleRequest> roles) {

        return roles
                .stream()
                .map(roleRequest -> iRoleRepository.findByNome(roleRequest.getNome()))
                .collect(Collectors.toList());
    }

    public UsuarioCadastroResponse updateUsuario(String guid, UsuarioRequest usuarioRequest) {

        UsuarioEntity usuario = iUsuarioRepository.findByGuid(guid)
                .orElseThrow(()-> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        if (!usuarioRequest.getUserName().isEmpty())
            usuario.setUserName(usuarioRequest.getUserName());

        if (!usuarioRequest.getPassword().isEmpty())
            usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));

        if (!usuarioRequest.getEmail().isEmpty())
            usuario.setEmail(usuarioRequest.getEmail());

        if (!usuarioRequest.getRoles().isEmpty()) {
            usuario.setRoles(addRoles(usuarioRequest.getRoles()));
        }

        return mapper.usuarioEntityToUsuarioCadastroResponse(iUsuarioRepository.save(usuario));
    }

    public void apagarUsuario(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(()-> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        this.iUsuarioRepository.delete(usuario);
    }

    public UsuarioCadastroResponse buscarUsuarioByGuid(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(()-> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        return mapper.usuarioEntityToUsuarioCadastroResponse(usuario);
    }
}

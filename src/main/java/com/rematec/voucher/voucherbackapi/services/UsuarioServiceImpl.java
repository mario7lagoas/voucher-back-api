package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.services.IUsuarioService;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioResponse;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.response.UsuariosPaginadaResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.UUID;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VoucherUtil voucherUtil;

    public List<UsuarioApiResponse> buscandoListaUsuario() {
        return this.mapper.listUsuarioEntityTolistUsuarioApiResponse(this.iUsuarioRepository.findAll());
    }

    public BuscandoListaPaginadaUsuario200Response buscandoListaPaginadaUsuario(String nome, Integer page, Integer size) {
        return this.mapper.pageUsuariosEntityToUsuariosApiPaginadaResponse(
                this.iUsuarioRepository.findByUserNameContaining(nome, PageRequest.of(page, size)));
    }

    public UsuarioApiResponse buscandoUsuarioPeloGUID(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));
        return this.mapper.usuarioEntityToUsuarioApiResponse(usuario);
    }

    public UsuarioApiResponse criandoUsuario(UsuarioApiRequest usuarioApiRequest) {

        if (!this.voucherUtil.checkDataNullAndEmpty(usuarioApiRequest.getEmail())) {
            throw new BadRequestException("E-mail do usuário obrigatório.");
        }

        if (!this.voucherUtil.checkDataNullAndEmpty(usuarioApiRequest.getPassword())) {
            throw new BadRequestException("Senha do usuário obrigatório.");
        }

        if (this.iUsuarioRepository.findByEmail(usuarioApiRequest.getEmail()).isPresent()) {
            throw new UsuarioCadastradoException("E-mail já cadastrado.");
        }

        if (!this.voucherUtil.checkDataNullAndEmpty(usuarioApiRequest.getUserName())) {
            throw new BadRequestException("Nome do usuário obrigatório.");
        }

        if (usuarioApiRequest.getStatus() == null) {
            throw new BadRequestException("Status do usuário obrigatório.");
        }

        if (usuarioApiRequest.getPerfis() == null || usuarioApiRequest.getPerfis().isEmpty()) {
            throw new BadRequestException("Perfil do usuário obrigatório.");
        }

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .guid(UUID.randomUUID().toString())
                .userName(usuarioApiRequest.getUserName())
                .email(usuarioApiRequest.getEmail())
                .perfis(this.voucherUtil.listUsuarioPerfilApiRequestToListPerfilEntity(usuarioApiRequest.getPerfis()))
                .status(usuarioApiRequest.getStatus())
                .password(this.passwordEncoder.encode(usuarioApiRequest.getPassword()))
                .build();

        return this.mapper.usuarioEntityToUsuarioApiResponse(this.iUsuarioRepository.save(usuarioEntity));
    }

    @Override
    public List<UsuarioResponse> getAllUsuarios() {
        List<UsuarioEntity> entities = this.iUsuarioRepository.findAll();
        return mapper.listUsuarioEntityTolistUsuarioResponse(entities);
    }

    @Override
    public UsuarioResponse addUsuario(UsuarioRequest usuarioRequest) {
        if (this.iUsuarioRepository.findByEmail(usuarioRequest.getEmail()).isPresent()) {
            throw new UsuarioCadastradoException("E-mail já cadastrado.");
        }

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .guid(UUID.randomUUID().toString())
                .userName(usuarioRequest.getUserName())
                .email(usuarioRequest.getEmail())
                .perfis(this.voucherUtil.listPerfisRequestToListPerfilEntity(usuarioRequest.getPerfis()))
                .status(usuarioRequest.getStatus())
                .password(passwordEncoder.encode(usuarioRequest.getPassword()))
                .build();

        return mapper.usuarioEntityToUsuarioResponse(this.iUsuarioRepository.save(usuarioEntity));
    }

    @Override
    public UsuarioResponse updateUsuario(String guid, UsuarioRequest usuarioRequest) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        if (this.voucherUtil.checkDataNullAndEmpty(usuarioRequest.getUserName()))
            usuario.setUserName(usuarioRequest.getUserName());

        if (this.voucherUtil.checkDataNullAndEmpty(usuarioRequest.getPassword()))
            usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));

        if (this.voucherUtil.checkDataNullAndEmpty(usuarioRequest.getEmail()))
            usuario.setEmail(usuarioRequest.getEmail());

        if (usuarioRequest.getPerfis() != null && !usuarioRequest.getPerfis().isEmpty()) {
            usuario.setPerfis(this.voucherUtil.listPerfisRequestToListPerfilEntity(usuarioRequest.getPerfis()));
        }
        if (usuarioRequest.getStatus() != null) {
            usuario.setStatus(usuarioRequest.getStatus());
        }

        return mapper.usuarioEntityToUsuarioResponse(this.iUsuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse buscarUsuarioByGuid(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        return mapper.usuarioEntityToUsuarioResponse(usuario);
    }

    @Override
    public void apagarUsuario(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        this.iUsuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioResponse updateStatus(String guid, UpdateStatusResquest statusResquest) {

        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        usuario.setStatus(statusResquest.getStatus());

        return mapper.usuarioEntityToUsuarioResponse(this.iUsuarioRepository.save(usuario));
    }

    @Override
    public UsuariosPaginadaResponse obterUsuarioPaginadas(String nome, int page, int size) {

        return mapper.pageUsuariosEntityToUsuariosPaginadaResponse(
                this.iUsuarioRepository.findByUserNameContaining(nome, PageRequest.of(page, size)));
    }


}

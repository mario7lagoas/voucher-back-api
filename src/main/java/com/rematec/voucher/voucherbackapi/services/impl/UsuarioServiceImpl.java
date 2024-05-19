package com.rematec.voucher.voucherbackapi.services.impl;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.services.UsuarioService;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class UsuarioServiceImpl extends UsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VoucherUtil voucherUtil;

    @Override
    public List<UsuarioApiResponse> buscandoListaUsuario() {
        return this.mapper.listUsuarioEntityTolistUsuarioApiResponse(this.iUsuarioRepository.findAll());
    }

    @Override
    public BuscandoListaPaginadaUsuario200Response buscandoListaPaginadaUsuario(String nome, Integer page, Integer size) {
        return this.mapper.pageUsuariosEntityToUsuariosApiPaginadaResponse(
                this.iUsuarioRepository.findByUserNameContaining(nome, PageRequest.of(page, size)));
    }

    @Override
    public UsuarioApiResponse buscandoUsuarioPeloGUID(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));
        return this.mapper.usuarioEntityToUsuarioApiResponse(usuario);
    }

    @Override
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
        if (usuarioApiRequest.getStatus() == null) {
            throw new BadRequestException("Status do usuário obrigatório.");
        }

        if (usuarioApiRequest.getPerfis() == null || usuarioApiRequest.getPerfis().isEmpty()) {
            throw new BadRequestException("Perfil do usuário obrigatório.");
        }

        if (!this.voucherUtil.checkDataNullAndEmpty(usuarioApiRequest.getUserName())) {
            throw new BadRequestException("Nome do usuário obrigatório.");
        }

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .guid(UUID.randomUUID().toString())
                .userName(usuarioApiRequest.getUserName())
                .email(usuarioApiRequest.getEmail())
                .perfis(this.voucherUtil.listUsuarioPerfilApiRequestToListPerfilEntity(usuarioApiRequest.getPerfis()))
                .status(usuarioApiRequest.getStatus())
                .lojas(this.voucherUtil.getListGuidApiRequestToListLojasEntity(usuarioApiRequest.getLojas()))
                .password(this.passwordEncoder.encode(usuarioApiRequest.getPassword()))
                .build();

        return this.mapper.usuarioEntityToUsuarioApiResponse(this.iUsuarioRepository.save(usuarioEntity));
    }

    @Override
    public UsuarioApiResponse alterandoUsuario(String guid, UsuarioUpdateApiRequest usuarioUpdateApiRequest) {

        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        if (this.voucherUtil.checkDataNullAndEmpty(usuarioUpdateApiRequest.getUserName()))
            usuario.setUserName(usuarioUpdateApiRequest.getUserName());

        if (this.voucherUtil.checkDataNullAndEmpty(usuarioUpdateApiRequest.getPassword()))
            usuario.setPassword(passwordEncoder.encode(usuarioUpdateApiRequest.getPassword()));

        if (this.voucherUtil.checkDataNullAndEmpty(usuarioUpdateApiRequest.getEmail())) {

            if (this.iUsuarioRepository.findByEmail(usuarioUpdateApiRequest.getEmail()).isPresent()) {
                if (!guid.equals(this.iUsuarioRepository.findByEmail(usuarioUpdateApiRequest.getEmail()).get().getGuid()))
                    throw new UsuarioCadastradoException("E-mail já cadastrado.");
            }
            usuario.setEmail(usuarioUpdateApiRequest.getEmail());
        }

        if (usuarioUpdateApiRequest.getStatus() != null)
            usuario.setStatus(usuarioUpdateApiRequest.getStatus());

        if (usuarioUpdateApiRequest.getPerfis() != null && !usuarioUpdateApiRequest.getPerfis().isEmpty()) {
            usuario.setPerfis(
                    this.voucherUtil.listUsuarioPerfilApiRequestToListPerfilEntity(usuarioUpdateApiRequest.getPerfis())
            );
        }

        if (usuarioUpdateApiRequest.getLojas() != null && !usuarioUpdateApiRequest.getLojas().isEmpty()) {
            usuario.getLojas().clear();
            usuario.getLojas().addAll(
                    this.voucherUtil.getListGuidApiRequestToListLojasEntity(usuarioUpdateApiRequest.getLojas())
            );
        } else {
            usuario.setLojas(null);
        }

        return this.mapper.usuarioEntityToUsuarioApiResponse(this.iUsuarioRepository.save(usuario));

    }

    @Override
    public void apagandoUsuario(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        this.iUsuarioRepository.delete(usuario);
    }

    @Override
    public void alterandoStatusUsuario(String guid, UpdateStatusApiRequest updateStatusApiRequest) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        usuario.setStatus(updateStatusApiRequest.getStatus());

        this.mapper.usuarioEntityToUsuarioApiResponse(this.iUsuarioRepository.save(usuario));
    }
}

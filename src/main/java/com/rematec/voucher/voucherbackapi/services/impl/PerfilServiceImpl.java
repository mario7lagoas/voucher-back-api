package com.rematec.voucher.voucherbackapi.services.impl;

import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.PerfilUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirPerfilException;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.services.PerfilService;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PerfilServiceImpl extends PerfilService {

    @Autowired
    private IPerfilRepository iPerfilRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private VoucherUtil voucherUtil;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Override
    public List<PerfilApiResponse> buscandoListaPerfil() {
        return this.mapper.listPerfilEntityToListPerfilApiResponse(this.iPerfilRepository.findAll());
    }

    @Override
    public List<PerfilResumidoApiResponse> buscandoListaResumidoPerfil() {
        return this.mapper.listPerfilEntityToListPerfilResumidoApiResponse(this.iPerfilRepository.findAll());
    }

    @Override
    public PerfilApiResponse buscandoPerfilPeloGUID(String guid) {

        PerfilEntity entity = this.iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        return this.mapper.perfilEntityToPerfilApiResponse(entity);
    }

    @Override
    public PerfilApiResponse buscandoPerfilPeloNome(String nome) {

        PerfilEntity entity = this.iPerfilRepository.findByNome(nome)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        return this.mapper.perfilEntityToPerfilApiResponse(entity);
    }

    @Override
    public PerfilApiResponse criandoPerfil(PerfilApiRequest perfilApiRequest) {


        if (!this.voucherUtil.checkDataNullAndEmpty(perfilApiRequest.getNome())) {
            throw new BadRequestException("Nome do perfil obrigatório.");
        }

        if (perfilApiRequest.getRoles() == null || perfilApiRequest.getRoles().isEmpty()) {
            throw new BadRequestException("Permissão do Perfil é Obrigatório.");
        }

        if (this.iPerfilRepository.findByNome(perfilApiRequest.getNome()).isPresent()) {
            throw new PerfilCadastradoException("Já existe um Perfil com este nome.");
        }

        PerfilEntity perfilEntity = PerfilEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(perfilApiRequest.getNome())
                .roles(this.voucherUtil.listRoleApiResponseToListRoleEntity(perfilApiRequest.getRoles()))
                .build();
        return this.mapper.perfilEntityToPerfilApiResponse(this.iPerfilRepository.save(perfilEntity));

    }

    @Override
    public PerfilApiResponse alterandoPerfil(String guid, PerfilUpdateApiRequest perfilApiRequest) {

        PerfilEntity entity = this.iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        if (this.voucherUtil.checkDataNullAndEmpty(perfilApiRequest.getNome())) {
            entity.setNome(perfilApiRequest.getNome());
        }

        if (perfilApiRequest.getRoles() != null && !perfilApiRequest.getRoles().isEmpty()) {
            entity.getRoles().clear();
            entity.getRoles().addAll(this.voucherUtil.listRoleApiResponseToListRoleEntity(perfilApiRequest.getRoles()));
        }

        return this.mapper.perfilEntityToPerfilApiResponse(this.iPerfilRepository.save(entity));

    }

    @Override
    public void apagandoPerfil(String guid) {

        PerfilEntity entity = this.iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        if (this.iUsuarioRepository.findTop1ByPerfisGuid(guid).isPresent()) {
            throw new NaoPermitidoExcluirPerfilException("Não permitido Excluir. Perfil associado a algum Usuario.");
        }
        this.iPerfilRepository.delete(entity);

    }

}

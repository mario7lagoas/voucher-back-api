package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirPerfilException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.interfaces.services.IPerfilService;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PerfilServiceImpl implements IPerfilService {

    @Autowired
    private IPerfilRepository iPerfilRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private VoucherUtil voucherUtil;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    public List<PerfilApiResponse> getAllPerfilTeste() {
        return mapper.listPerfilEntityToListPerfilApiResponse(this.iPerfilRepository.findAll());
    }

    @Override
    public List<PerfilResponse> getAllPerfil() {
        return mapper.listPerfilEntityToListPerfilResponse(this.iPerfilRepository.findAll());
    }

    @Override
    public List<PerfilResumidoResponse> getAllPerfilResumido() {
        return mapper.listPerfilEntityToListPerfilResumidoResponse(this.iPerfilRepository.findAll());
    }

    @Override
    public PerfilResponse addPerfil(PerfilRequest request) {

        if (this.iPerfilRepository.findByNome(request.getNome()).isPresent()) {
            throw new PerfilCadastradoException("Já existe um Perfil com este nome.");
        }
        PerfilEntity perfilEntity = PerfilEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(request.getNome())
                .roles(voucherUtil.listRolesRequestToListRoleEntity(request.getRoles()))
                .build();
        return mapper.perfilEntityToPerfilResponse(this.iPerfilRepository.save(perfilEntity));
    }

    @Override
    public PerfilResponse getPerfilNome(String nome) {
        PerfilEntity entity = iPerfilRepository.findByNome(nome)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        return mapper.perfilEntityToPerfilResponse(entity);
    }

    @Override
    public PerfilResponse getPerfilGuid(String guid) {

        PerfilEntity entity = iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        return mapper.perfilEntityToPerfilResponse(entity);
    }

    @Override
    public PerfilResponse alterarPerfil(String guid, PerfilRequest request) {

        PerfilEntity entity = iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PerfilNaoEncontradoException("Perfil não encontrado."));

        if (this.voucherUtil.checkDataNullAndEmpty(request.getNome())) {
            entity.setNome(request.getNome());
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            entity.getRoles().clear();
            entity.getRoles().addAll(voucherUtil.listRolesRequestToListRoleEntity(request.getRoles()));
        }

        return mapper.perfilEntityToPerfilResponse(iPerfilRepository.save(entity));
    }

    @Override
    public void apagarPerfil(String guid) {

        PerfilEntity entity = this.iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado."));

        if (this.iUsuarioRepository.findTop1ByPerfisGuid(guid).isPresent()) {
            throw new NaoPermitidoExcluirPerfilException("Não permitido Excluir. Perfil associado a algum Usuario.");
        }
        this.iPerfilRepository.delete(entity);

    }
}

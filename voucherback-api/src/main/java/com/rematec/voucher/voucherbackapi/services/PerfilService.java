package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirPerfilException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.PromocaoNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IRoleRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    @Autowired
    private IPerfilRepository iPerfilRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private IRoleRepository iRoleRepository;

    public List<PerfilResponse> getAllPeril() {

        return mapper.listPerfilEntityToListPerfilResponse(this.iPerfilRepository.findAll());

    }

    public List<PerfilResumidoResponse> getAllPerilResumido() {

        return mapper.listPerfilEntityToListPerfilResumidoResponse(this.iPerfilRepository.findAll());
    }

    public PerfilResponse addPerfil(PerfilRequest request) {

        if (iPerfilRepository.findByNome(request.getNome()).isPresent()) {
            throw new PerfilCadastradoException("Já existe um Perfil com este nome");
        }

        PerfilEntity perfilEntity = PerfilEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(request.getNome())
                .roles(addRoles(request.getRoles()))
                .build();

        return mapper.perfilEntityToPerfilResponse(iPerfilRepository.save(perfilEntity));

    }

    public PerfilResponse getPerfilNome(String nome) {
        PerfilEntity entity = iPerfilRepository.findByNome(nome)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Perfil não encontrado"));


        return mapper.perfilEntityToPerfilResponse(entity);
    }

    private List<RoleEntity> addRoles(List<RoleRequest> roles) {

        return roles
                .stream()
                .map(roleRequest -> iRoleRepository.findByNome(roleRequest.getNome()))
                .collect(Collectors.toList());
    }

    public PerfilResponse getPerfilGuid(String guid) {

        PerfilEntity entity = iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Perfil não encontrado"));

        return mapper.perfilEntityToPerfilResponse(entity);
    }

    public PerfilResponse alterarPerfil(String guid, PerfilRequest request) {

        PerfilEntity entity = iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Perfil não encontrado"));

        if (checkData(request.getNome())) {
            entity.setNome(request.getNome());
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            entity.getRoles().clear();
            entity.getRoles().addAll(addRoles(request.getRoles()));
        }

        return mapper.perfilEntityToPerfilResponse(iPerfilRepository.save(entity));
    }

    private boolean checkData(String data) {

        if (data != null && !data.isEmpty())
            return true;

        return false;
    }


    public void apagarPerfil(String guid) {

        PerfilEntity entity = this.iPerfilRepository.findByGuid(guid)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado."));

        try {
            this.iPerfilRepository.delete(entity);
        }catch (Exception ex){
            throw new NaoPermitidoExcluirPerfilException("Não permitido Excluir. Perfil associado a algum Usuario");
        }



    }
}

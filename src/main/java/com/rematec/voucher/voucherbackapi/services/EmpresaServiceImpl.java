package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.EmpresaApiRequest;
import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.models.EmpresaResumidoApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.EmpresaCadastradaException;
import com.rematec.voucher.voucherbackapi.exceptios.EmpresaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirEmpresaException;
import com.rematec.voucher.voucherbackapi.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import com.rematec.voucher.voucherbackapi.repositories.IEmpresaRepository;
import com.rematec.voucher.voucherbackapi.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class EmpresaServiceImpl implements IEmpresaService {

    private final IEmpresaRepository iEmpresaRepository;
    private final IUsuarioRepository iUsuarioRepository;
    private final VouckBackMapper mapper;
    private final VoucherUtil voucherUtil;

    public EmpresaServiceImpl(final IEmpresaRepository iEmpresaRepository, final IUsuarioRepository iUsuarioRepository,
                              final VouckBackMapper mapper, final VoucherUtil voucherUtil) {
        this.iEmpresaRepository = iEmpresaRepository;
        this.iUsuarioRepository = iUsuarioRepository;
        this.mapper = mapper;
        this.voucherUtil = voucherUtil;
    }

    @Override
    public List<EmpresaApiResponse> buscandoListaEmpresa() {
        return this.mapper.listEmpresaEntityToListEmpresaApiResponse(this.iEmpresaRepository.findAll());
    }

    @Override
    public EmpresaApiResponse criandoEmpresa(EmpresaApiRequest empresaApiRequest) {

        if (this.iEmpresaRepository.findByIdentificacao(empresaApiRequest.getIdentificacao()).isPresent()) {
            throw new EmpresaCadastradaException("Empresa já cadastrada.");
        }

        EmpresaEntity empresaEntity = EmpresaEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(empresaApiRequest.getNome())
                .identificacao(empresaApiRequest.getIdentificacao())
                .status(empresaApiRequest.getStatus())
                .build();

        return this.mapper.empresaEntityToEmpresaApiResponse(this.iEmpresaRepository.save(empresaEntity));

    }

    @Override
    public EmpresaApiResponse buscandoEmpresaPeloGUID(String guid) {

        EmpresaEntity empresaEntity = this.iEmpresaRepository.findByGuid(guid)
                .orElseThrow(() -> new EmpresaNaoEncontradaException("Empresa não encontrada."));

        return this.mapper.empresaEntityToEmpresaApiResponse(empresaEntity);
    }

    @Override
    public EmpresaApiResponse alterandoEmpresa(String guid, EmpresaApiRequest empresaApiRequest) {

        EmpresaEntity empresaEntity = this.iEmpresaRepository.findByGuid(guid)
                .orElseThrow(() -> new EmpresaNaoEncontradaException("Empresa não encontrada."));

        if (empresaApiRequest.getStatus() != null)
            empresaEntity.setStatus(empresaApiRequest.getStatus());

        if (this.voucherUtil.checkDataNullAndEmpty(empresaApiRequest.getIdentificacao()))
            empresaEntity.setIdentificacao(empresaApiRequest.getIdentificacao());

        if (this.voucherUtil.checkDataNullAndEmpty(empresaApiRequest.getNome()))
            empresaEntity.setNome(empresaApiRequest.getNome());

        return this.mapper.empresaEntityToEmpresaApiResponse(this.iEmpresaRepository.save(empresaEntity));
    }

    @Override
    public void apagandoEmpresa(String guid) {
        EmpresaEntity empresaEntity = this.iEmpresaRepository.findByGuid(guid)
                .orElseThrow(() -> new EmpresaNaoEncontradaException("Empresa não encontrada."));

        if (!this.iUsuarioRepository.findByEmpresaGuid(guid).isEmpty()) {
            throw new NaoPermitidoExcluirEmpresaException("Empresa não pode ser Excluida.");
        }

        this.iEmpresaRepository.delete(empresaEntity);
    }

    @Override
    public List<EmpresaResumidoApiResponse> buscandoListaEmpresaResumido() {
        return this.mapper.listEmpresaEntityToListEmpresaResumidoApiResponse(this.iEmpresaRepository.findAll());
    }
}

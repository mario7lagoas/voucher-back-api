package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.EmpresaApiRequest;
import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.EmpresaCadastradaException;
import com.rematec.voucher.voucherbackapi.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import com.rematec.voucher.voucherbackapi.repositories.IEmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
class EmpresaServiceImpl implements IEmpresaService{

    @Autowired
    private IEmpresaRepository iEmpresaRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Override
    public List<EmpresaApiResponse> buscandoListaEmpresa() {
        return this.mapper.listEmpresaEntityToListEmpresaApiResponse(this.iEmpresaRepository.findAll());
    }

    @Override
    public EmpresaApiResponse criandoEmpresa(EmpresaApiRequest empresaApiRequest) {

        if(this.iEmpresaRepository.findByIdentificacao(empresaApiRequest.getIdentificacao()).isPresent()){
            throw new EmpresaCadastradaException("Emporesa já cadastrada.");
        }

        EmpresaEntity empresaEntity = EmpresaEntity.builder()
                .nome(empresaApiRequest.getNome())
                .identificacao(empresaApiRequest.getIdentificacao())
                .status(empresaApiRequest.getStatus())
                .build();

        return this.mapper.empresaEntityToEmpresaApiResponse(this.iEmpresaRepository.save(empresaEntity));

    }
}

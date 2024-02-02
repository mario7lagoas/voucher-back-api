package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirLojaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaReposity;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.requests.LojaRequest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class LojaService {

    @Autowired
    private ILojaReposity iLojaReposity;

    @Autowired
    private VouckBackMapper mapper;

    public LojaResponse addLoja(LojaRequest lojaRequest) {
        LojaEntity lojaEntity = LojaEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(lojaRequest.getNome())
                .cnpj(lojaRequest.getCnpj())
                .identificacao(lojaRequest.getIdentificacao())
                .build();

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));
    }

    public List<LojaResponse> gelAll() {
        return mapper.listLojaEntityToListLojaResponse(iLojaReposity.findAll());
    }

    public LojaResponse updateLoja(String guid, LojaRequest lojaRequest) {
        LojaEntity lojaEntity = iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));


        if (!lojaRequest.getCnpj().isEmpty())
            lojaEntity.setCnpj(lojaRequest.getCnpj());

        if (!lojaRequest.getIdentificacao().isEmpty())
            lojaEntity.setIdentificacao(lojaRequest.getIdentificacao());
        if (!lojaRequest.getNome().isEmpty())
            lojaEntity.setNome(lojaRequest.getNome());

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));
    }

    public void apagarLoja(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        if (!this.iLojaReposity.findByPromocoesLojasGuid(guid).isEmpty())
            throw new NaoPermitidoExcluirLojaException("Loja não pode ser Excluida. Pois está associada a alguma promoção");

        this.iLojaReposity.delete(lojaEntity);
    }

    public LojaResponse buscarLojaByGuid(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        return mapper.lojaEntityToLojaResponse(lojaEntity);
    }
}

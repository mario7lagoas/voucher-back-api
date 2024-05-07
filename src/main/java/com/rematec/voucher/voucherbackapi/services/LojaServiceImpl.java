package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.LojaCadastradaException;
import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirLojaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.interfaces.services.ILojaService;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.requests.LojaRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.LojasPaginadaResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class LojaServiceImpl implements ILojaService {

    @Autowired
    private ILojaRepository iLojaReposity;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private VoucherUtil voucherUtil;

    public List<LojaApiResponse> buscandoListaLoja() {
        return mapper.listLojaEntityToListLojaApiResponse(iLojaReposity.findAll());
    }

    public BuscandoListaPaginadaLoja200Response buscandoListaPaginadaLoja(String cnpj, Integer page, Integer size) {

        return this.mapper.pageLojasEntityToLojasPaginadaApiResponse(
                this.iLojaReposity.findByCnpjContaining(this.voucherUtil.apenasNumerosNaString(cnpj),
                        PageRequest.of(page, size))
        );
    }

    public List<LojaApiResponse> buscandoListaLojaAtiva() {
        return this.mapper.listLojaEntityToListLojaApiResponse(iLojaReposity.findByStatusTrue());
    }

    public LojaApiResponse buscandoLojaPeloGUID(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        return this.mapper.lojaEntityToLojaApiResponse(lojaEntity);
    }


    @Override
    public List<LojaResponse> getAll() {
        return mapper.listLojaEntityToListLojaResponse(iLojaReposity.findAll());
    }



    @Override
    public LojaResponse addLoja(LojaRequest lojaRequest) {
        if (iLojaReposity.findByCnpj(lojaRequest.getCnpj()).isPresent()) {
            throw new LojaCadastradaException("CNPJ Já cadastrado.");
        }

        LojaEntity lojaEntity = LojaEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(lojaRequest.getNome())
                .status(lojaRequest.getStatus())
                .cnpj(this.voucherUtil.apenasNumerosNaString(lojaRequest.getCnpj()))
                .identificacao(lojaRequest.getIdentificacao())
                .build();

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));

    }
    @Override
    public LojaResponse updateLoja(String guid, LojaRequest lojaRequest) {
        LojaEntity lojaEntity = iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        if (!lojaRequest.getCnpj().isEmpty())
            lojaEntity.setCnpj(lojaRequest.getCnpj());

        if (!lojaRequest.getIdentificacao().isEmpty())
            lojaEntity.setIdentificacao(lojaRequest.getIdentificacao());
        if (!lojaRequest.getNome().isEmpty())
            lojaEntity.setNome(lojaRequest.getNome());

        if (lojaRequest.getStatus() != null)
            lojaEntity.setStatus(lojaRequest.getStatus());

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));
    }

    @Override
    public void apagarLoja(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        if (!this.iLojaReposity.findByPromocoesLojasGuid(guid).isEmpty())
            throw new NaoPermitidoExcluirLojaException("Loja não pode ser Excluida. Pois está associada a alguma promoção");

        this.iLojaReposity.delete(lojaEntity);
    }

    @Override
    public LojaResponse buscarLojaByGuid(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        return mapper.lojaEntityToLojaResponse(lojaEntity);
    }

    @Override
    public LojaResponse updateStatus(String guid, UpdateStatusResquest statusResquest) {

        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        lojaEntity.setStatus(statusResquest.getStatus());
        log.info("Atualizando status da loja [{}]", lojaEntity.getNome());

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));

    }

    @Override
    public List<LojaResponse> getLojasAtivas() {
        return mapper.listLojaEntityToListLojaResponse(iLojaReposity.findByStatusTrue());
    }

    @Override
    public LojasPaginadaResponse obterLojasPaginadas(String cnpj, int page, int size) {

        return mapper.pageLojasEntityToLojasPaginadaResponse(
                this.iLojaReposity.findByCnpjContaining(cnpj.replaceAll("[^0-9]", ""),
                        PageRequest.of(page, size))
        );
    }



}

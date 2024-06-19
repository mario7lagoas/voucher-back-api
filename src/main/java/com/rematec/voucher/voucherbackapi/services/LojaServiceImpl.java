package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.LojaApiRequest;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.LojaUpdateApiRequest;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.EmpresaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.LojaCadastradaException;
import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirLojaException;
import com.rematec.voucher.voucherbackapi.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import com.rematec.voucher.voucherbackapi.repositories.IEmpresaRepository;
import com.rematec.voucher.voucherbackapi.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
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
class LojaServiceImpl implements ILojaService {

    @Autowired
    private ILojaRepository iLojaReposity;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private VoucherUtil voucherUtil;

    @Autowired
    private IEmpresaRepository iEmpresaRepository;

    @Override
    public List<LojaApiResponse> buscandoListaLoja() {
        return this.mapper.listLojaEntityToListLojaApiResponse(iLojaReposity.findAll());
    }

    @Override
    public BuscandoListaPaginadaLoja200Response buscandoListaPaginadaLoja(String cnpj, Integer page, Integer size) {

        return this.mapper.pageLojasEntityToLojasPaginadaApiResponse(
                this.iLojaReposity.findByCnpjContaining(this.voucherUtil.apenasNumerosNaString(cnpj),
                        PageRequest.of(page, size))
        );
    }

    @Override
    public List<LojaApiResponse> buscandoListaLojaAtiva(String email) {
        return this.mapper.listLojaEntityToListLojaApiResponse(
                email.isEmpty() ?
                        this.iLojaReposity.findByStatusTrue() : this.iLojaReposity.findByStatusTrueAndUsuariosEmail(email)
        );
    }

    @Override
    public LojaApiResponse buscandoLojaPeloGUID(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada."));

        return this.mapper.lojaEntityToLojaApiResponse(lojaEntity);
    }

    @Override
    public LojaApiResponse criandoLoja(LojaApiRequest lojaApiRequest) {

        if (!this.voucherUtil.checkDataNullAndEmpty(lojaApiRequest.getCnpj())) {
            throw new BadRequestException("CNPJ da loja obrigatório.");
        }

        if (iLojaReposity.findByCnpj(lojaApiRequest.getCnpj()).isPresent()) {
            throw new LojaCadastradaException("CNPJ Já cadastrado.");
        }

        if (!this.voucherUtil.checkDataNullAndEmpty(lojaApiRequest.getNome())) {
            throw new BadRequestException("Nome da loja obrigatório.");
        }

        if (!this.voucherUtil.checkDataNullAndEmpty(lojaApiRequest.getIdentificacao())) {
            throw new BadRequestException("Identificação da loja obrigatório.");
        }

        if (!this.voucherUtil.checkDataNullAndEmpty(lojaApiRequest.getEmpresa())) {
            throw new BadRequestException("Identificacão da Empresa é Obrigatorio.");
        }

        EmpresaEntity empresaEntity = this.iEmpresaRepository.findByGuid(lojaApiRequest.getEmpresa())
                .orElseThrow(()-> new EmpresaNaoEncontradaException("Empresa não encontrada."));


        LojaEntity lojaEntity = LojaEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(lojaApiRequest.getNome())
                .status(lojaApiRequest.getStatus())
                .cnpj(this.voucherUtil.apenasNumerosNaString(lojaApiRequest.getCnpj()))
                .identificacao(lojaApiRequest.getIdentificacao())
                .empresa(empresaEntity)
                .build();

        return mapper.lojaEntityToLojaApiResponse(this.iLojaReposity.save(lojaEntity));

    }

    @Override
    public LojaApiResponse alterandoLoja(String guid, LojaUpdateApiRequest lojaApiRequest) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada."));

        if (!lojaApiRequest.getCnpj().isEmpty()) {
            String cnpj = this.voucherUtil.apenasNumerosNaString(lojaApiRequest.getCnpj());
            if (this.iLojaReposity.findByCnpj(cnpj).isPresent()) {
                if (!guid.equals(this.iLojaReposity.findByCnpj(cnpj).get().getGuid()))
                    throw new LojaCadastradaException("CNPJ Já cadastrado.");
            }
            lojaEntity.setCnpj(cnpj);
        }

        if (!lojaApiRequest.getIdentificacao().isEmpty())
            lojaEntity.setIdentificacao(lojaApiRequest.getIdentificacao());

        if (!lojaApiRequest.getNome().isEmpty())
            lojaEntity.setNome(lojaApiRequest.getNome());

        if (lojaApiRequest.getStatus() != null)
            lojaEntity.setStatus(lojaApiRequest.getStatus());

        return mapper.lojaEntityToLojaApiResponse(iLojaReposity.save(lojaEntity));

    }

    @Override
    public void apagandoLoja(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada."));

        if (!this.iLojaReposity.findByPromocoesLojasGuid(guid).isEmpty())
            throw new NaoPermitidoExcluirLojaException("Loja não pode ser Excluida. Pois está associada a alguma promoção.");

        if (!this.iLojaReposity.findByUsuariosLojasGuid(guid).isEmpty())
            throw new NaoPermitidoExcluirLojaException("Loja não pode ser Excluida. Pois está associada a algum Usuário.");

        this.iLojaReposity.delete(lojaEntity);
    }

    @Override
    public void alterandoStatusLoja(String guid, UpdateStatusApiRequest updateStatusApiRequest) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada."));

        lojaEntity.setStatus(updateStatusApiRequest.getStatus());
        log.info("Atualizando status da loja [{}]", lojaEntity.getNome());

        this.iLojaReposity.save(lojaEntity);
    }

}

package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.models.ConsultaVoucherApiRequest;
import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.models.VoucherApiRequest;
import com.rematec.voucher.models.VoucherApiResponse;
import com.rematec.voucher.models.VoucherFinalizeApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.builders.ConsultaVoucherApiResponseBuilder;
import com.rematec.voucher.voucherbackapi.builders.VoucherPromocaoApiResponseBuilder;
import com.rematec.voucher.voucherbackapi.exceptios.VoucherNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.exceptios.VoucherNaoPermitidoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IVoucherRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherPromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import com.rematec.voucher.voucherbackapi.models.filter.VoucherFiltro;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
class VoucherServiceImpl implements IVoucherService {

    @Autowired
    private IVoucherRepository iVoucherRepository;

    @Autowired
    private IPromocaoRepository iPromocaoRepository;

    @Autowired
    private VoucherUtil voucherUtil;

    @Autowired
    private VouckBackMapper mapper;


    @Override
    public ConsultaVoucherApiResponse consultandoPromocoes(ConsultaVoucherApiRequest consulta) {

        ConsultaVoucherApiResponse consultaVoucherResponse = ConsultaVoucherApiResponseBuilder.builder().status("VOID")
                .totalVoucher(0).build();

        List<PromocaoEntity> promocaoEntities = iPromocaoRepository
                .findByInicioLessThanEqualAndFimGreaterThanEqualAndValorMinimoParaDisparoLessThanEqualAndPromocaoStatusAndLojasCnpjAndLojasStatusTrue(
                        LocalDateTime.now(), LocalDateTime.now(), consulta.getValorCompra(), PromocaoStatusEnum.ATIVA,
                        this.voucherUtil.apenasNumerosNaString(consulta.getFilialCnpj())
                );

        if (!promocaoEntities.isEmpty()) {

            final boolean[] promocaoDisponivel = new boolean[1];
            promocaoDisponivel[0] = false;

            List<VoucherApiResponse> voucherResponses = new ArrayList<>();
            promocaoEntities.forEach(promocaoEntity -> {
                if (!this.iVoucherRepository.findTop1ByClienteCpfEqualsAndPromocaoGuidAndVoucherStatusNot(
                        this.voucherUtil.apenasNumerosNaString(consulta.getClienteCpf()), promocaoEntity.getGuid(),
                        VoucherStatusEnum.CANCELADO).isPresent()) {

                    VoucherEntity voucherEntity = VoucherEntity.builder()
                            .guid(UUID.randomUUID().toString())
                            .codigo(this.voucherUtil.gerarCodigoVoucher(consulta.getPdvFilial()))
                            .clienteCpf(this.voucherUtil.apenasNumerosNaString(consulta.getClienteCpf()))
                            .filialCnpj(this.voucherUtil.apenasNumerosNaString(consulta.getFilialCnpj()))
                            .pdv(consulta.getPdvFilial())
                            .cupom(consulta.getCupom())
                            .descricao(promocaoEntity.getDescricao())
                            .valorDesconto(promocaoEntity.getTipoDesconto().name().equals("VALOR") ? promocaoEntity.getDescontoValor() : promocaoEntity.getDescontoPercentual())
                            .voucherStatus(VoucherStatusEnum.DISPONIBILIZADO)
                            .promocaoStatus(VoucherPromocaoStatusEnum.DISPONIVEL)
                            .diasValidadeVoucher(promocaoEntity.getDiasValidadeVoucher())
                            .fimResgate(promocaoEntity.getFim().plusDays(promocaoEntity.getDiasValidadeVoucher()))
                            .promocaoGuid(promocaoEntity.getGuid())
                            .valorMaximoDesconto(promocaoEntity.getValorMaximoDesconto())
                            .tipoDesconto(promocaoEntity.getTipoDesconto())
                            .inicio(promocaoEntity.getInicio())
                            .fim(promocaoEntity.getFim())
                            .build();

                    VoucherApiResponse voucherApiResponse = mapper.voucherEntityToVoucherApiResponse(
                            this.iVoucherRepository.save(voucherEntity));

                    voucherResponses.add(voucherApiResponse);
                    promocaoDisponivel[0] = true;
                }
            });
            if (promocaoDisponivel[0]) {
                consultaVoucherResponse.setStatus("OK");
                consultaVoucherResponse.setVouchers(voucherResponses);
                consultaVoucherResponse.setTotalVoucher(voucherResponses.size());
            }
        }

        return consultaVoucherResponse;
    }

    @Async("threadPollConfirmandoVoucherExecutor")
    @Override
    public void confirmandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        this.voucherUtil.cancelOrConfirmVoucherApi(voucherApiRequest, VoucherStatusEnum.CONFIRMADO);
    }

    @Async("threadPollCancelandoVoucherExecutor")
    @Override
    public void cancelandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        this.voucherUtil.cancelOrConfirmVoucherApi(voucherApiRequest, VoucherStatusEnum.CANCELADO);
    }

    @Override
    public VoucherPromocaoApiResponse resgatandoVoucher(VoucherPromocaoApiRequest promocaoRequest) {
        VoucherEntity voucherEntity = this.iVoucherRepository
                .findByCodigoEqualsAndFimResgateGreaterThanEqualAndVoucherStatus(
                        promocaoRequest.getCodigo(), LocalDateTime.now(), VoucherStatusEnum.CONFIRMADO)
                .orElseThrow(() -> new VoucherNaoEncontradoException("Voucher nao [" + promocaoRequest.getCodigo()
                        + "] encontrado"));

        this.voucherUtil.checkStatusVoucher(voucherEntity);

        this.iPromocaoRepository.findByGuidAndLojasCnpjAndLojasStatusTrue(voucherEntity.getPromocaoGuid(),
                        this.voucherUtil.apenasNumerosNaString(promocaoRequest.getFilialCnpj()))
                .orElseThrow(() -> new VoucherNaoEncontradoException("Promocao indisponivel para "
                        + this.voucherUtil.getLojaNome(promocaoRequest.getFilialCnpj())));

        VoucherPromocaoApiResponse voucherPromocaoResponse = VoucherPromocaoApiResponseBuilder.builder()
                .status("OK")
                .descricao(voucherEntity.getDescricao())
                .transacao(voucherEntity.getGuid())
                .build();

        if (voucherEntity.getTipoDesconto().name().equals("PERCENTUAL")) {

            BigDecimal desconto = getValorformatado(promocaoRequest.getValorPagamento(), voucherEntity.getValorDesconto(),
                    voucherEntity.getTipoDesconto().name());

            voucherPromocaoResponse.setValorDesconto(
                    desconto.compareTo(voucherEntity.getValorMaximoDesconto()) < 0 ? desconto : voucherEntity.getValorMaximoDesconto()
            );

        } else {
            voucherPromocaoResponse.setValorDesconto(voucherEntity.getValorDesconto());
        }

        if (voucherPromocaoResponse.getValorDesconto().compareTo(promocaoRequest.getValorPagamento()) == 1) {
            throw new VoucherNaoPermitidoException("Desconto [" + voucherPromocaoResponse.getValorDesconto()
                    + "] maior que pagamento [" + promocaoRequest.getValorPagamento() + "]");
        }

        voucherEntity.setCupomResgate(promocaoRequest.getCupom());
        voucherEntity.setFilialCnpjResgate(promocaoRequest.getFilialCnpj());
        voucherEntity.setValorPagamento(promocaoRequest.getValorPagamento());
        voucherEntity.setPdvResgate(promocaoRequest.getPdv());
        voucherEntity.setPromocaoStatus(VoucherPromocaoStatusEnum.EM_USO);
        this.iVoucherRepository.save(voucherEntity);

        return voucherPromocaoResponse;
    }

    @Async("threadPollConfirmandoVoucherExecutor")
    @Override
    public void consumindoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest) {

        VoucherEntity entity = this.getVoucherEntityPosVenda(voucherFinalizeApiRequest);
        if (entity != null) {
            entity.setDataResgate(LocalDateTime.now());
            entity.setVoucherStatus(VoucherStatusEnum.UTILIZADO);
            entity.setPromocaoStatus(VoucherPromocaoStatusEnum.UTILIZADO);
            entity.setValorPago(voucherFinalizeApiRequest.getValorPago());
            log.warn("Baixa no Voucher {} .", voucherFinalizeApiRequest.getTransacao());
            this.iVoucherRepository.save(entity);
        }
    }

    @Async("threadPollCancelandoVoucherExecutor")
    @Override
    public void estornandoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest) {

        VoucherEntity entity = this.getVoucherEntityPosVenda(voucherFinalizeApiRequest);
        if (entity != null) {
            entity.setVoucherStatus(VoucherStatusEnum.CONFIRMADO);
            entity.setPromocaoStatus(VoucherPromocaoStatusEnum.DISPONIVEL);
            log.warn("Estorno no Voucher {} .", voucherFinalizeApiRequest.getTransacao());
            this.iVoucherRepository.save(entity);

        }
    }

    @Override
    public BuscandoListaFiltroVoucher200Response buscandoListaFiltroVoucher(
            Integer page, Integer size, String codigo, String descricao, String clienteCpf, String pdv,
            String cupomResgate, String inicio, String fim, String voucherStatus, String filialCnpj, String tipoDesconto) {
        VoucherFiltro filtro = VoucherFiltro.builder()
                .codigo(this.voucherUtil.apenasNumerosNaString(codigo))
                .descricao(descricao)
                .clienteCpf(this.voucherUtil.apenasNumerosNaString(clienteCpf))
                .tipoDesconto(tipoDesconto)
                .filialCnpj(this.voucherUtil.apenasNumerosNaString(filialCnpj))
                .pdv(pdv)
                .cupomResgate(cupomResgate)
                .inicio(inicio != null && !inicio.isEmpty() ? LocalDate.parse(inicio) : null)
                .voucherStatus(voucherStatus)
                .fim(fim != null && !fim.isEmpty() ? LocalDate.parse(fim) : null)
                .build();

        return this.iVoucherRepository.filtrar(filtro, PageRequest.of(page, size));
    }

    private BigDecimal getValorformatado(BigDecimal compra, BigDecimal desconto, String tipo) {

        BigDecimal valor = BigDecimal.ZERO;

        if ("PERCENTUAL".equals(tipo)) {
            valor = (compra.multiply(desconto).divide(BigDecimal.valueOf(100)));
        }
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    private VoucherEntity getVoucherEntityPosVenda(VoucherFinalizeApiRequest voucher) {
        if (this.iVoucherRepository.findByGuid(voucher.getTransacao()).isPresent()) {
            VoucherEntity entity = this.iVoucherRepository.findByGuid(voucher.getTransacao()).get();
            if (entity.getPromocaoStatus().name().equals("EM_USO")) {
                return entity;
            } else {
                log.warn("Voucher {} não está com status de EM_USO, status atual {}.", voucher.getTransacao(),
                        entity.getPromocaoStatus().name());
            }
        } else {
            log.warn("Voucher {} não ecnontrado", voucher.getTransacao());
        }
        return null;

    }
}

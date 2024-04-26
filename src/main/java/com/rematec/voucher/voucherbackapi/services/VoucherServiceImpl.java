package com.rematec.voucher.voucherbackapi.services;

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
import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherFinalizeRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherPromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherPromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;
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
public class VoucherServiceImpl {

    @Autowired
    private IPromocaoRepository iPromocaoRepository;

    @Autowired
    private VoucherUtil voucherUtil;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private IVoucherRepository iVoucherRepository;

    public ConsultaVoucherResponse consultarPromocoes(ConsultaVoucherRequest consulta) {

        ConsultaVoucherResponse consultaVoucherResponse = ConsultaVoucherResponse
                .builder().status("VOID")
                .totalVoucher(0)
                .build();

        List<PromocaoEntity> promocaoEntities = iPromocaoRepository
                .findByInicioLessThanEqualAndFimGreaterThanEqualAndValorMinimoParaDisparoLessThanEqualAndPromocaoStatusAndLojasCnpjAndLojasStatusTrue(
                        LocalDateTime.now(), LocalDateTime.now(), consulta.getValorCompra(), PromocaoStatusEnum.ATIVA,
                        this.voucherUtil.apenasNumerosNaString(consulta.getFilialCnpj()));

        if (!promocaoEntities.isEmpty()) {

            final boolean[] promocaoDisponivel = new boolean[1];
            promocaoDisponivel[0] = false;

            List<VoucherResponse> voucherResponses = new ArrayList<>();

            promocaoEntities.forEach(promocaoEntity -> {
                if (!this.iVoucherRepository.findTop1ByClienteCpfEqualsAndPromocaoGuidAndVoucherStatusNot(
                        this.voucherUtil.apenasNumerosNaString(consulta.getClienteCpf()), promocaoEntity.getGuid(),
                        VoucherStatusEnum.CANCELADO).isPresent()) {

                    VoucherEntity voucherEntity = mapper.promocaoEntityToVoucherEntity(promocaoEntity,
                            VoucherStatusEnum.DISPONIBILIZADO, VoucherPromocaoStatusEnum.DISPONIVEL,
                            UUID.randomUUID().toString(), this.voucherUtil.gerarCodigoVoucher(consulta.getPdvFilial()),
                            this.voucherUtil.apenasNumerosNaString(consulta.getClienteCpf()),
                            this.voucherUtil.apenasNumerosNaString(consulta.getFilialCnpj()),
                            promocaoEntity.getTipoDesconto().name().equals("VALOR") ? promocaoEntity.getDiscontoValor() : promocaoEntity.getDiscontoPercentual(),
                            consulta.getPdvFilial(), promocaoEntity.getFim().plusDays(promocaoEntity.getDiasValidadeVoucher()),
                            consulta.getCupom()
                    );

                    VoucherResponse voucherResponse = mapper.voucherEntityToVoucherResponse(
                            this.iVoucherRepository.save(voucherEntity));

                    voucherResponses.add(voucherResponse);
                    promocaoDisponivel[0] = true;
                }
            });
            if (promocaoDisponivel[0]) {
                consultaVoucherResponse.setStatus("OK");
                consultaVoucherResponse.setTotalVoucher(promocaoEntities.size());

                consultaVoucherResponse.setVouchers(voucherResponses);
            }
        }

        return consultaVoucherResponse;
    }

    @Async("threadPollConfirmandoVoucherExecutor")
    public void confirmarVoucher(List<VoucherRequest> voucherRequests) {

        this.voucherUtil.cancelOrConfirmVoucher(voucherRequests, VoucherStatusEnum.CONFIRMADO);

    }

    @Async("threadPollCancelandoVoucherExecutor")
    public void cancelarVoucher(List<VoucherRequest> voucherRequests) {
        this.voucherUtil.cancelOrConfirmVoucher(voucherRequests, VoucherStatusEnum.CANCELADO);
    }

    @Async("threadPollConfirmandoVoucherExecutor")
    public void consumer(VoucherFinalizeRequest voucher) {

        VoucherEntity entity = getVoucherPosVenda(voucher);
        if (entity != null) {
            entity.setVoucherStatus(VoucherStatusEnum.UTILIZADO);
            entity.setPromocaoStatus(VoucherPromocaoStatusEnum.UTILIZADO);
            log.warn("Baixa no Voucher {} .", voucher.getTransacao());
            this.iVoucherRepository.save(entity);
        }
    }

    @Async("threadPollCancelandoVoucherExecutor")
    public void rollback(VoucherFinalizeRequest voucher) {
        VoucherEntity entity = getVoucherPosVenda(voucher);
        if (entity != null) {
            entity.setVoucherStatus(VoucherStatusEnum.UTILIZADO);
            entity.setPromocaoStatus(VoucherPromocaoStatusEnum.UTILIZADO);
            log.warn("Rollback no Voucher {} .", voucher.getTransacao());
            this.iVoucherRepository.save(entity);

        }
    }

    public VoucherPromocaoResponse resgateVoucher(VoucherPromocaoRequest promocaoRequest) {

        VoucherEntity voucherEntity = this.iVoucherRepository
                .findByCodigoEqualsAndFimResgateGreaterThanEqualAndVoucherStatus(
                        promocaoRequest.getCodigo(), LocalDateTime.now(), VoucherStatusEnum.CONFIRMADO)
                .orElseThrow(() -> new VoucherNaoEncontradoException("Voucher não [" + promocaoRequest.getCodigo()
                        + "] encontrado"));

        this.voucherUtil.checkStatusVoucher(voucherEntity);

        this.iPromocaoRepository.findByGuidAndLojasCnpjAndLojasStatusTrue(voucherEntity.getPromocaoGuid(),
                        this.voucherUtil.apenasNumerosNaString(promocaoRequest.getFilialCnpj()))
                .orElseThrow(() -> new VoucherNaoEncontradoException("Promoção não disponivel para o CNPJ ["
                        + promocaoRequest.getFilialCnpj()));

        VoucherPromocaoResponse voucherPromocaoResponse = VoucherPromocaoResponse.builder()
                .status("OK")
                .descricao(voucherEntity.getDescricao())
                .transacao(voucherEntity.getGuid())
                .build();

        if (voucherEntity.getTipoDesconto().name().equals("PERCENTUAL")) {
            voucherPromocaoResponse.setValorDesconto(getValorformatado(promocaoRequest.getValorPagamento(),
                    voucherEntity.getValorDesconto(), voucherEntity.getTipoDesconto().name()));
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

    private BigDecimal getValorformatado(BigDecimal compra, BigDecimal desconto, String tipo) {

        BigDecimal valor = BigDecimal.ZERO;

        if ("PERCENTUAL".equals(tipo)) {
            valor = (compra.multiply(desconto).divide(BigDecimal.valueOf(100)));
        }
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    private VoucherEntity getVoucherPosVenda(VoucherFinalizeRequest voucher) {

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

    public VouchersPaginadaResponse voucherFiltro(int page, int size, String codigo, String descricao, String clienteCpf
            , String pdv, String cupomResgate, LocalDate inicio, LocalDate fim, String voucherStatus, String filialCnpj,
                                                  String tipoDesconto) {

        VoucherFiltro filtro = VoucherFiltro.builder()
                .codigo(this.voucherUtil.apenasNumerosNaString(codigo))
                .descricao(descricao)
                .clienteCpf(this.voucherUtil.apenasNumerosNaString(clienteCpf))
                .tipoDesconto(tipoDesconto)
                .filialCnpj(this.voucherUtil.apenasNumerosNaString(filialCnpj))
                .pdv(pdv)
                .cupomResgate(cupomResgate)
                .inicio(inicio)
                .voucherStatus(voucherStatus)
                .fim(fim).build();

        return this.iVoucherRepository.filtrar(filtro, PageRequest.of(page,size));
    }
}

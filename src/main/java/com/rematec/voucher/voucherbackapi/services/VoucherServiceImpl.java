package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IVoucherRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
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
                .findByInicioLessThanEqualAndFimGreaterThanEqualAndValorMinimoParaDisparoGreaterThanEqualAndPromocaoStatusAndLojasCnpjAndLojasStatusTrue(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        consulta.getValorCompra(),
                        PromocaoStatusEnum.ATIVA,
                        this.voucherUtil.apenasNumerosNaString(consulta.getFilialCnpj()));

        if (!promocaoEntities.isEmpty()) {

            final boolean[] promocaoDisponivel = new boolean[1];
            promocaoDisponivel[0] = false;

            List<VoucherResponse> voucherResponses = new ArrayList<>();

            promocaoEntities.forEach(p -> {
                if (!this.iVoucherRepository.findTop1ByClienteCpfEqualsAndPromocaoGuidAndVoucherStatus(
                        this.voucherUtil.apenasNumerosNaString(consulta.getClienteCpf()), p.getGuid(),
                        VoucherStatusEnum.DISPONIBILIZADO).isPresent()) {

                    VoucherEntity voucherEntity = mapper.promocaoEntityToVoucherEntity(p,
                            VoucherStatusEnum.DISPONIBILIZADO,
                            UUID.randomUUID().toString(),
                            voucherUtil.gerarCodigoVoucher(consulta.getPdvFilial()),
                            this.voucherUtil.apenasNumerosNaString(consulta.getClienteCpf()),
                            this.voucherUtil.apenasNumerosNaString(consulta.getFilialCnpj()),
                            p.getTipoDesconto().name().equals("VALOR") ? p.getDiscontoValor() : p.getDiscontoPercentual(),
                            consulta.getPdvFilial()
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

}

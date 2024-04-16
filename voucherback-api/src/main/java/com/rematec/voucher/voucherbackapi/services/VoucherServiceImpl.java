package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ConsultaVoucherResponse consultarPromocoes(ConsultaVoucherRequest consulta) {

        ConsultaVoucherResponse consultaVoucherResponse = ConsultaVoucherResponse
                .builder().status("VOID")
                .guid("")
                .build();

        List<PromocaoEntity> promocaoEntities = iPromocaoRepository
                .findByInicioLessThanEqualAndFimGreaterThanEqualAndValorMinimoParaDisparoGreaterThanEqualAndPromocaoStatusAndLojasCnpjAndLojasStatusTrue(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        consulta.getValorCompra(),
                        PromocaoStatusEnum.ATIVA,
                        consulta.getCnpjFilial());

        if (!promocaoEntities.isEmpty()) {
            consultaVoucherResponse.setStatus("OK");
            consultaVoucherResponse.setGuid(UUID.randomUUID().toString());

            List<VoucherResponse> voucherResponses = new ArrayList<>();
            promocaoEntities.forEach(p -> {
                VoucherResponse voucherResponse = VoucherResponse
                        .builder()
                        .descricao(p.getDescricao())
                        .inicio(p.getInicio())
                        .fim(p.getFim())
                        .tipoDesconto(p.getTipoDesconto().toString())
                        .valorDesconto(
                                p.getTipoDesconto().name().equals("VALOR") ? p.getDiscontoValor() : p.getDiscontoPercentual()
                        ).build();
                voucherResponses.add(voucherResponse);
            });

            consultaVoucherResponse.setVouchers(voucherResponses);
        }

        return consultaVoucherResponse;
    }
}

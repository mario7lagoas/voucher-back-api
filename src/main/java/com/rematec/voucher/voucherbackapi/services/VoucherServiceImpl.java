package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IVoucherRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
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
                        consulta.getCnpjFilial());

        if (!promocaoEntities.isEmpty()) {
            consultaVoucherResponse.setStatus("OK");
            consultaVoucherResponse.setTotalVoucher(promocaoEntities.size());
            List<VoucherResponse> voucherResponses = new ArrayList<>();
            promocaoEntities.forEach(p -> {

                VoucherEntity voucherEntity = mapper.promocaoEntityToVoucherEntity(p,
                        VoucherStatusEnum.DISPONIBILIZADO,
                        UUID.randomUUID().toString(),
                        voucherUtil.gerarCodigoVoucher(consulta.getPdvFilial()),
                        consulta.getCpfCliente(),
                        consulta.getCnpjFilial(),
                        p.getTipoDesconto().name().equals("VALOR") ? p.getDiscontoValor() : p.getDiscontoPercentual()
                );

                VoucherResponse voucherResponse = mapper.voucherEntityToVoucherResponse(
                        this.iVoucherRepository.save(voucherEntity));

                voucherResponses.add(voucherResponse);
            });

            consultaVoucherResponse.setVouchers(voucherResponses);
        }

        return consultaVoucherResponse;
    }
}

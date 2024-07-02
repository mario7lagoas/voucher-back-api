package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PromocaoApiRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.GuidApiRequestBuilder.umGuidApiRequest;

public class PromocaoApiRequestBuilder {

    private PromocaoApiRequest promocaoApiRequest;

    private PromocaoApiRequestBuilder() {
    }

    public static PromocaoApiRequestBuilder umaPromocaoApiRequest() {

        PromocaoApiRequestBuilder builder = new PromocaoApiRequestBuilder();
        builder.promocaoApiRequest = new PromocaoApiRequest();
        builder.promocaoApiRequest.setDescricao("Promocao 01");
        builder.promocaoApiRequest.setInicio(LocalDateTime.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        builder.promocaoApiRequest.setFim(LocalDateTime.now().plusDays(2)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        builder.promocaoApiRequest.setTipoDesconto("PERCENTUAL");
        builder.promocaoApiRequest.setPromocaoStatus("ATIVA");
        builder.promocaoApiRequest.setDescontoPercentual(BigDecimal.valueOf(3.0));
        builder.promocaoApiRequest.setDescontoValor(BigDecimal.ZERO);
        builder.promocaoApiRequest.setValorMaximoDesconto(BigDecimal.valueOf(10.00));
        builder.promocaoApiRequest.setValorMinimoParaDisparo(BigDecimal.valueOf(50.00));
        builder.promocaoApiRequest.setDiasValidadeVoucher(3);
        builder.promocaoApiRequest.setAutorAlteracao("Any athor");
        return builder;
    }
    public PromocaoApiRequestBuilder empresa(String empresa) {
        promocaoApiRequest.setEmpresa(empresa);
        return this;
    }

    public PromocaoApiRequestBuilder inicio(String inicio) {
        promocaoApiRequest.setInicio(inicio);
        return this;
    }

    public PromocaoApiRequestBuilder fim(String fim) {
        promocaoApiRequest.setFim(fim);
        return this;
    }

    public PromocaoApiRequest agora() {
        return promocaoApiRequest;
    }

}

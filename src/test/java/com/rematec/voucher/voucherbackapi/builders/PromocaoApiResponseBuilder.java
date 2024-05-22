package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PromocaoApiResponse;

import java.math.BigDecimal;
import java.util.Collections;

import static com.rematec.voucher.voucherbackapi.builders.LojaApiResponseBuilder.umaLojaApiResponse;

public class PromocaoApiResponseBuilder {
    public PromocaoApiResponse promocaoApiResponse;
    private PromocaoApiResponseBuilder(){}

    public static PromocaoApiResponseBuilder umaPromocaoApiResponse(){
        PromocaoApiResponseBuilder builder = new PromocaoApiResponseBuilder();
        builder.promocaoApiResponse = new PromocaoApiResponse();
        builder.promocaoApiResponse.setGuid("aaaa-bbbb-cccc-dddd-eeeee");
        builder.promocaoApiResponse.setDescricao("Promocao de Natal");
        builder.promocaoApiResponse.setInicio("2024-05-12 09:56:13");
        builder.promocaoApiResponse.setFim("2024-05-31 09:56:16");
        builder.promocaoApiResponse.setTipoDesconto("PERCENTUAL");
        builder.promocaoApiResponse.setPromocaoStatus("ATIVA");
        builder.promocaoApiResponse.setDescontoValor(BigDecimal.ZERO);
        builder.promocaoApiResponse.setDescontoPercentual(BigDecimal.valueOf(5));
        builder.promocaoApiResponse.setValorMaximoDesconto(BigDecimal.valueOf(10));
        builder.promocaoApiResponse.setValorMinimoParaDisparo(BigDecimal.valueOf(50));
        builder.promocaoApiResponse.setDiasValidadeVoucher(3);
        builder.promocaoApiResponse.setAutorAlteracao("Any Thor");
        return builder;
    }

    public PromocaoApiResponseBuilder comLoja(){
        this.promocaoApiResponse.setLojas(Collections.singletonList(umaLojaApiResponse().agora()));
        return this;
    }

    public PromocaoApiResponse agora(){
        return promocaoApiResponse;
    }
}

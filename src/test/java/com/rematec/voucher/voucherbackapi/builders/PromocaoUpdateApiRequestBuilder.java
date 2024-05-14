package com.rematec.voucher.voucherbackapi.builders;


import com.rematec.voucher.models.PromocaoUpdateApiRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.GuidApiRequestBuilder.umGuidApiRequest;

public class PromocaoUpdateApiRequestBuilder {

    private PromocaoUpdateApiRequest promocaoApiRequest;

    private PromocaoUpdateApiRequestBuilder() {
    }

    public static PromocaoUpdateApiRequestBuilder umaPromocaoUpdateApiRequest() {

        PromocaoUpdateApiRequestBuilder builder = new PromocaoUpdateApiRequestBuilder();
        builder.promocaoApiRequest = new PromocaoUpdateApiRequest();
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

    public PromocaoUpdateApiRequestBuilder descricao(String nome) {
        promocaoApiRequest.setDescricao(nome);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder inicio(String inicio) {
        promocaoApiRequest.setInicio(inicio);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder fim(String fim) {
        promocaoApiRequest.setFim(fim);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder tipoDesconto(String tipodesconto) {
        promocaoApiRequest.setTipoDesconto(tipodesconto);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder promocaoStatus(String promocaoStatus) {
        promocaoApiRequest.setPromocaoStatus(promocaoStatus);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder descontoValor(BigDecimal decimal) {
        promocaoApiRequest.setDescontoValor(decimal);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder descontoPercentual(BigDecimal decimal) {
        promocaoApiRequest.setDescontoPercentual(decimal);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder valorMaximoDesconto(BigDecimal decimal) {
        promocaoApiRequest.setValorMaximoDesconto(decimal);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder valorMinimoParaDisparo(BigDecimal decimal) {
        promocaoApiRequest.setValorMinimoParaDisparo(decimal);
        return this;
    }

    public PromocaoUpdateApiRequestBuilder comLoja() {
        promocaoApiRequest.setLojas(Arrays.asList(umGuidApiRequest().agora()));
        return this;
    }

    public PromocaoUpdateApiRequest agora() {
        return promocaoApiRequest;
    }

}

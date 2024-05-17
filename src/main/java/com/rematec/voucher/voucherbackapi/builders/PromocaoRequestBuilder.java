package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PromocaoApiRequest;

import java.math.BigDecimal;

public class PromocaoRequestBuilder {

    protected PromocaoApiRequest request;

    private PromocaoRequestBuilder() {
    }

    public static PromocaoRequestBuilder builder() {
        PromocaoRequestBuilder builder = new PromocaoRequestBuilder();
        builder.request = new PromocaoApiRequest();
        return builder;
    }

    public PromocaoRequestBuilder valorMaximoDesconto(BigDecimal decimal) {
        request.setValorMaximoDesconto(decimal);
        return this;
    }

    public PromocaoRequestBuilder tipoDesconto(String tipoDesconto) {
        request.setTipoDesconto(tipoDesconto);
        return this;
    }

    public PromocaoApiRequest buider() {
        return request;
    }
}

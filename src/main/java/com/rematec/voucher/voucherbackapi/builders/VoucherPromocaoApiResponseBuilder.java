package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.VoucherPromocaoApiResponse;

import java.math.BigDecimal;

public class VoucherPromocaoApiResponseBuilder {

    protected VoucherPromocaoApiResponse voucherPromocaoApiResponse;

    public static VoucherPromocaoApiResponseBuilder builder() {
        VoucherPromocaoApiResponseBuilder builder = new VoucherPromocaoApiResponseBuilder();
        builder.voucherPromocaoApiResponse = new VoucherPromocaoApiResponse();
        return builder;
    }

    public VoucherPromocaoApiResponseBuilder status(String status) {
        this.voucherPromocaoApiResponse.setStatus(status);
        return this;
    }

    public VoucherPromocaoApiResponseBuilder transacao(String transacao) {
        this.voucherPromocaoApiResponse.setTransacao(transacao);
        return this;
    }

    public VoucherPromocaoApiResponseBuilder descricao(String descricao) {
        this.voucherPromocaoApiResponse.setDescricao(descricao);
        return this;
    }

    public VoucherPromocaoApiResponseBuilder valorDesconto(BigDecimal valorDesconto) {
        this.voucherPromocaoApiResponse.setValorDesconto(valorDesconto);
        return this;
    }

    public VoucherPromocaoApiResponse build() {
        return this.voucherPromocaoApiResponse;
    }
}

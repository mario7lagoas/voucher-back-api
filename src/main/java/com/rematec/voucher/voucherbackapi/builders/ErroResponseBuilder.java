package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.ErroResponse;

public class ErroResponseBuilder {
    private ErroResponse erroResponse;

    private ErroResponseBuilder() {
    }

    public static ErroResponseBuilder builder() {
        ErroResponseBuilder builder = new ErroResponseBuilder();
        builder.erroResponse = new ErroResponse();
        return builder;
    }

    public ErroResponseBuilder codigo(String codigo) {
        erroResponse.setCodigo(codigo);
        return this;
    }

    public ErroResponseBuilder mensagem(String mensagem) {
        erroResponse.setMensagem(mensagem);
        return this;
    }

    public ErroResponse build() {
        return erroResponse;
    }
}

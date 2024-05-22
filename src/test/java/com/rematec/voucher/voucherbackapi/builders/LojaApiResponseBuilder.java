package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.LojaApiResponse;

public class LojaApiResponseBuilder {
    private LojaApiResponse lojaApiResponse;
    private LojaApiResponseBuilder(){}

    public static LojaApiResponseBuilder umaLojaApiResponse(){
        LojaApiResponseBuilder builder = new LojaApiResponseBuilder();
        builder.lojaApiResponse = new LojaApiResponse();
        builder.lojaApiResponse.setGuid("aaaa-bbbb-cccc-dddd-eeeee");
        builder.lojaApiResponse.setNome("Loja 001");
        builder.lojaApiResponse.setCnpj("4249270100013");
        builder.lojaApiResponse.setIdentificacao("0001");
        builder.lojaApiResponse.setStatus(true);
        builder.lojaApiResponse.setDataCadastro("2024-05-07 11:48:11");
        return builder;
    }

    public LojaApiResponse agora(){
        return lojaApiResponse;
    }
}

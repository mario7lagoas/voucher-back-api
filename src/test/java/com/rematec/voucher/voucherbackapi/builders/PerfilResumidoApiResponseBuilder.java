package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PerfilResumidoApiResponse;

public class PerfilResumidoApiResponseBuilder {
    private PerfilResumidoApiResponse perfilResumidoApiResponse = new PerfilResumidoApiResponse();
    private PerfilResumidoApiResponseBuilder(){}

    public static PerfilResumidoApiResponseBuilder umPerfilResumidoApiResponse(){
        PerfilResumidoApiResponseBuilder builder = new PerfilResumidoApiResponseBuilder();
        builder.perfilResumidoApiResponse = new PerfilResumidoApiResponse();
        builder.perfilResumidoApiResponse.setGuid("123456");
        builder.perfilResumidoApiResponse.setNome("Any Perfil");
        return builder;
    }

    public PerfilResumidoApiResponse agora(){
        return perfilResumidoApiResponse;
    }
}

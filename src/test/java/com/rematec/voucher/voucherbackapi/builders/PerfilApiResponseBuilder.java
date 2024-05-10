package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PerfilApiResponse;

public class PerfilApiResponseBuilder {

    private PerfilApiResponse perfilApiResponse;

    private PerfilApiResponseBuilder(){}

    public static PerfilApiResponseBuilder umPerfilApiResponse(){
        PerfilApiResponseBuilder builder = new PerfilApiResponseBuilder();
        builder.perfilApiResponse = new  PerfilApiResponse();
        builder.perfilApiResponse.setGuid("123456");
        builder.perfilApiResponse.setNome("One Perfil");
        return builder;
    }

    public PerfilApiResponseBuilder nome(String nome){
        perfilApiResponse.setNome(nome);
        return this;
    }

    public PerfilApiResponseBuilder guid(String guid){
        perfilApiResponse.setGuid(guid);
        return this;
    }

    public PerfilApiResponse agora(){
        return perfilApiResponse;
    }
}

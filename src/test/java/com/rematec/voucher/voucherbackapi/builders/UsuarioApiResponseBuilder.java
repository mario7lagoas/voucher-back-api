package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.UsuarioApiResponse;

public class UsuarioApiResponseBuilder {
    private UsuarioApiResponse usuarioApiResponse;
    private UsuarioApiResponseBuilder(){}

    public static UsuarioApiResponseBuilder umUsuarioApiResponse(){
        UsuarioApiResponseBuilder builder = new UsuarioApiResponseBuilder();
        builder.usuarioApiResponse = new UsuarioApiResponse();
        builder.usuarioApiResponse.setUserName("Any Name");
        builder.usuarioApiResponse.setGuid("123456789");
        builder.usuarioApiResponse.setEmail("any@email.com");
        builder.usuarioApiResponse.setStatus(true);
        return builder;
    }

    public UsuarioApiResponse agora(){
        return usuarioApiResponse;
    }
}

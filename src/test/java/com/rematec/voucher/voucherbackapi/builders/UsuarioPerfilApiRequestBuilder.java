package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.UsuarioPerfilApiRequest;

public class UsuarioPerfilApiRequestBuilder {

    private UsuarioPerfilApiRequest usuarioPerfilApiRequest;

    private UsuarioPerfilApiRequestBuilder(){

    }

    public static UsuarioPerfilApiRequestBuilder umUsuarioPerfilApiRequest(){

        UsuarioPerfilApiRequestBuilder builder = new UsuarioPerfilApiRequestBuilder();
        builder.usuarioPerfilApiRequest = new UsuarioPerfilApiRequest();
        builder.usuarioPerfilApiRequest.setNome("Any Description");
        return builder;
    }
    public UsuarioPerfilApiRequestBuilder nome(String nome){
        usuarioPerfilApiRequest.setNome(nome);
        return this;
    }

    public UsuarioPerfilApiRequest agora(){
        return usuarioPerfilApiRequest;
    }

}

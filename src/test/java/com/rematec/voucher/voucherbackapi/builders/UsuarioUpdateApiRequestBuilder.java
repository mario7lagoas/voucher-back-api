package com.rematec.voucher.voucherbackapi.builders;


import com.rematec.voucher.models.UsuarioUpdateApiRequest;

import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.UsuarioPerfilApiRequestBuilder.umUsuarioPerfilApiRequest;

public class UsuarioUpdateApiRequestBuilder {

    private UsuarioUpdateApiRequest usuarioUpdateApiRequest;

    private UsuarioUpdateApiRequestBuilder() {
    }

    public static UsuarioUpdateApiRequestBuilder umUsuarioUpdateApiRequest() {

        UsuarioUpdateApiRequestBuilder builder = new UsuarioUpdateApiRequestBuilder();
        builder.usuarioUpdateApiRequest = new UsuarioUpdateApiRequest();
        builder.usuarioUpdateApiRequest.setUserName("Any User");
        builder.usuarioUpdateApiRequest.setPassword("123456");
        builder.usuarioUpdateApiRequest.setEmail("anyemail@email.com");
        builder.usuarioUpdateApiRequest.setStatus(true);
        return builder;
    }

    public UsuarioUpdateApiRequestBuilder userName(String nome) {
        usuarioUpdateApiRequest.setUserName(nome);
        return this;
    }

    public UsuarioUpdateApiRequestBuilder empresa(String empresa) {
        usuarioUpdateApiRequest.setEmpresa(empresa);
        return this;
    }

    public UsuarioUpdateApiRequestBuilder status(Boolean status) {
        usuarioUpdateApiRequest.setStatus(status);
        return this;
    }

    public UsuarioUpdateApiRequestBuilder comPerfis() {
        usuarioUpdateApiRequest.setPerfis(Arrays.asList(umUsuarioPerfilApiRequest().agora()));
        return this;
    }


    public UsuarioUpdateApiRequest agora() {
        return usuarioUpdateApiRequest;
    }

}

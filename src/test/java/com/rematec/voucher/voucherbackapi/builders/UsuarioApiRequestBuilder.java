package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioPerfilApiRequest;

import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.UsuarioPerfilApiRequestBuilder.umUsuarioPerfilApiRequest;

public class UsuarioApiRequestBuilder {

    private UsuarioApiRequest usuarioApiRequest;

    private UsuarioApiRequestBuilder() {
    }

    public static UsuarioApiRequestBuilder umUsuarioApiRequest() {

        UsuarioApiRequestBuilder builder = new UsuarioApiRequestBuilder();
        builder.usuarioApiRequest = new UsuarioApiRequest();
        builder.usuarioApiRequest.setUserName("Any User");
        builder.usuarioApiRequest.setPassword("123456");
        builder.usuarioApiRequest.setEmail("anyemail@email.com");
        builder.usuarioApiRequest.setStatus(true);
        builder.usuarioApiRequest.setPerfis(Arrays.asList(umUsuarioPerfilApiRequest().agora()));
        return builder;
    }

    public UsuarioApiRequestBuilder userName(String nome) {
        usuarioApiRequest.setUserName(nome);
        return this;
    }

    public UsuarioApiRequestBuilder password(String pwd) {
        usuarioApiRequest.setPassword(pwd);
        return this;
    }

    public UsuarioApiRequestBuilder email(String email) {
        usuarioApiRequest.setEmail(email);
        return this;
    }

    public UsuarioApiRequestBuilder status(Boolean status) {
        usuarioApiRequest.setStatus(status);
        return this;
    }

    public UsuarioApiRequestBuilder perfis(UsuarioPerfilApiRequest perfil) {
        usuarioApiRequest.setPerfis(Arrays.asList(perfil));
        return this;
    }

    public UsuarioApiRequest agora() {
        return usuarioApiRequest;
    }

}

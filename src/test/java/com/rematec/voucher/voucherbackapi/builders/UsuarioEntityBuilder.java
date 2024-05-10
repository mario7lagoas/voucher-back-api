package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;

import java.time.LocalDateTime;

public class UsuarioEntityBuilder {

    private UsuarioEntity usuarioEntity;
    private UsuarioEntityBuilder(){}

    public static UsuarioEntityBuilder umUsuarioEntity(){

        UsuarioEntityBuilder builder = new UsuarioEntityBuilder();
        builder.usuarioEntity = new UsuarioEntity();
        builder.usuarioEntity.setId(1L);
        builder.usuarioEntity.setGuid("123456");
        builder.usuarioEntity.setUserName("Any User");
        builder.usuarioEntity.setEmail("anyemail@email.com");
        builder.usuarioEntity.setStatus(true);
        builder.usuarioEntity.setDataCadastro(LocalDateTime.now());
        builder.usuarioEntity.setDataAtualizacao(LocalDateTime.now());

        return builder;

    }
    public UsuarioEntityBuilder id(Long id){
        usuarioEntity.setId(id);
        return this;
    }

    public UsuarioEntityBuilder guid(String guid){
        usuarioEntity.setGuid(guid);
        return this;
    }

    public UsuarioEntityBuilder userName(String nome){
        usuarioEntity.setUserName(nome);
        return this;
    }

    public UsuarioEntity agora(){
        return usuarioEntity;
    }
}

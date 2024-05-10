package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;

import java.time.LocalDateTime;

public class LojaEntityBuilder {

    private LojaEntity lojaEntity;
    private LojaEntityBuilder(){}

    public static LojaEntityBuilder umaLojaEntity(){

        LojaEntityBuilder builder = new LojaEntityBuilder();
        builder.lojaEntity = new LojaEntity();
        builder.lojaEntity.setId(1L);
        builder.lojaEntity.setGuid("123456");
        builder.lojaEntity.setNome("Loja 001");
        builder.lojaEntity.setIdentificacao("0001");
        builder.lojaEntity.setCnpj("11111111111111");
        builder.lojaEntity.setStatus(true);
        builder.lojaEntity.setDataCadastro(LocalDateTime.now());
        builder.lojaEntity.setDataAtualizacao(LocalDateTime.now());

        return builder;

    }
    public LojaEntityBuilder id(Long id){
        lojaEntity.setId(id);
        return this;
    }

    public LojaEntityBuilder guid(String guid){
        lojaEntity.setGuid(guid);
        return this;
    }

    public LojaEntityBuilder nome(String nome){
        lojaEntity.setNome(nome);
        return this;
    }

    public LojaEntityBuilder cnpj(String nome){
        lojaEntity.setCnpj(nome);
        return this;
    }


    public LojaEntity agora(){
        return lojaEntity;
    }
}

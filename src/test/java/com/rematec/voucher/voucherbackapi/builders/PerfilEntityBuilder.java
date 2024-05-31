package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.enums.PermissaoEnum;

import java.time.LocalDateTime;
import java.util.Arrays;

public class PerfilEntityBuilder {

    private PerfilEntity perfilEntity;

    private PerfilEntityBuilder(){}

    public static PerfilEntityBuilder umPerfilEntity(){

        PerfilEntityBuilder builder = new PerfilEntityBuilder();
        builder.perfilEntity = new PerfilEntity();
        builder.perfilEntity.setId(1L);
        builder.perfilEntity.setGuid("123456");
        builder.perfilEntity.setNome("My Perfil");
        builder.perfilEntity.setDataCadastro(LocalDateTime.now());
        builder.perfilEntity.setDataAtualizacao(LocalDateTime.now());

        return builder;

    }
    public PerfilEntityBuilder setGuid(String guid){
        perfilEntity.setGuid(guid);
        return this;
    }

    public PerfilEntityBuilder setNome(String nome){
        perfilEntity.setNome(nome);
        return this;
    }

    public PerfilEntityBuilder comRoles(){
        perfilEntity.setRoles(Arrays.asList(RoleEntity.builder().id(1L).nome(PermissaoEnum.MENU_LOJA).build(),
                RoleEntity.builder().id(2L).nome(PermissaoEnum.MENU_USUARIO).build()));
        return this;
    }

    public PerfilEntity agora(){
        return perfilEntity;
    }
}

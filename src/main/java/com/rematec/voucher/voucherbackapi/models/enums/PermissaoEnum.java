package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PermissaoEnum {
    MODERADOR(1, "Moderador", "ROLE_MODERADOR"),
    ADMINISTRADOR(2, "Administrador","ROLE_ADMINISTRADOR" ),
    MASTER(3, "Usuário Master", "ROLE_MASTER"),
    GERENCIAL(4, "Perfil Gerencial", "ROLE_GERENCIAL"),
    USUARIO(5,"Usuario", "ROLE_USUARIO"),

    //Permissão Menus
    MENU_LOJA(6, "Menu Loja", "ROLE_MENU_LOJA"),
    MENU_USUARIO(7, "Menu Usario", "ROLE_MENU_USUARIO"),
    MENU_PROMOCAO(8, "Menu Promoção", "ROLE_MENU_PROMOCAO"),
    MENU_PERFIL(9, "Menu Perfil", "ROLE_MENU_PERFIL"),

    //Permissão Lojas
    BUSCAR_LOJA(10, "Buscar Loja", "ROLE_BUSCAR_LOJA"),
    ALTERAR_LOJA(11, "Editar Loja", "ROLE_ALTERAR_LOJA"),
    CADASTRAR_LOJA(12, "Cadastrar Loja", "ROLE_CADASTRAR_LOJA"),
    APAGAR_LOJA(13, "Apagar Loja", "ROLE_APAGAR_LOJA"),


    //Permissão Usuarios
    BUSCAR_USUARIO(14, "Buscar Usuário", "ROLE_BUSCAR_USUARIO"),
    ALTERAR_USUARIO(15, "Editar Usuário", "ROLE_ALTERAR_USUARIO"),
    CADASTRAR_USUARIO(16, "Cadastrar Usuário", "ROLE_CADASTRAR_USUARIO"),
    APAGAR_USUARIO(17, "Apagar Usuario", "ROLE_APAGAR_USUARIO"),

    //Permissão Promoção
    BUSCAR_PROMOCAO(18, "Buscar Promoção", "ROLE_BUSCAR_PROMOCAO"),
    ALTERAR_PROMOCAO(19, "Editar Promoção", "ROLE_ALTERAR_PROMOCAO"),
    CADASTRAR_PROMOCAO(20, "Cadastrar Promoção", "ROLE_CADASTRAR_PROMOCAO"),
    APAGAR_PROMOCAO(21, "Apagar Promoção", "ROLE_APAGAR_PROMOCAO"),

    //Permissão Perfis
    BUSCAR_PERFIL(22, "Buscar Perfil", "ROLE_BUSCAR_PERFIL"),
    ALTERAR_PERFIL(23, "Editar Perfil", "ROLE_ALTERAR_PERFIL"),
    CADASTRAR_PERFIL(24, "Cadastrar Perfil", "ROLE_CADASTRAR_PERFIL"),
    APAGAR_PERFIL(25, "Apagar Perfil", "ROLE_APAGAR_PERFIL");



    private int codigo;
    private String nome;
    private String role;

    public static PermissaoEnum toEnum(Integer codigo) {
        if (codigo == null) {
            return null;
        }
       for (PermissaoEnum x : PermissaoEnum.values()){
           if (codigo.equals(x.getCodigo())){
               return x;
           }
       }
       throw new IllegalArgumentException("Id inválido: " + codigo);
    }


}

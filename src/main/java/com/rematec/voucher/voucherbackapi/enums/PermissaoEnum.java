package com.rematec.voucher.voucherbackapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PermissaoEnum {

    //Permissão Menus
    MENU_LOJA(1, "Menu Loja", "ROLE_MENU_LOJA"),
    MENU_USUARIO(2, "Menu Usario", "ROLE_MENU_USUARIO"),
    MENU_PROMOCAO(3, "Menu Promoção", "ROLE_MENU_PROMOCAO"),
    MENU_PERFIL(4, "Menu Perfil", "ROLE_MENU_PERFIL"),
    MENU_VOUCHER(5, "Menu Transacao", "ROLE_MENU_VOUCHER"),

    //Permissão Lojas
    BUSCAR_LOJA(6, "Buscar Loja", "ROLE_BUSCAR_LOJA"),
    ALTERAR_LOJA(7, "Editar Loja", "ROLE_ALTERAR_LOJA"),
    CADASTRAR_LOJA(8, "Cadastrar Loja", "ROLE_CADASTRAR_LOJA"),
    APAGAR_LOJA(9, "Apagar Loja", "ROLE_APAGAR_LOJA"),


    //Permissão Usuarios
    BUSCAR_USUARIO(10, "Buscar Usuário", "ROLE_BUSCAR_USUARIO"),
    ALTERAR_USUARIO(11, "Editar Usuário", "ROLE_ALTERAR_USUARIO"),
    CADASTRAR_USUARIO(12, "Cadastrar Usuário", "ROLE_CADASTRAR_USUARIO"),
    APAGAR_USUARIO(13, "Apagar Usuario", "ROLE_APAGAR_USUARIO"),

    //Permissão Promoção
    BUSCAR_PROMOCAO(14, "Buscar Promoção", "ROLE_BUSCAR_PROMOCAO"),
    ALTERAR_PROMOCAO(15, "Editar Promoção", "ROLE_ALTERAR_PROMOCAO"),
    CADASTRAR_PROMOCAO(16, "Cadastrar Promoção", "ROLE_CADASTRAR_PROMOCAO"),
    APAGAR_PROMOCAO(17, "Apagar Promoção", "ROLE_APAGAR_PROMOCAO"),

    //Permissão Perfis
    BUSCAR_PERFIL(18, "Buscar Perfil", "ROLE_BUSCAR_PERFIL"),
    ALTERAR_PERFIL(19, "Editar Perfil", "ROLE_ALTERAR_PERFIL"),
    CADASTRAR_PERFIL(20, "Cadastrar Perfil", "ROLE_CADASTRAR_PERFIL"),
    APAGAR_PERFIL(21, "Apagar Perfil", "ROLE_APAGAR_PERFIL"),

    //Permissão Voucher
    BUSCAR_VOUCHER(22, "Buscar Voucher", "ROLE_BUSCAR_VOUCHER"),
    ALTERAR_VOUCHER(23, "Editar Voucher", "ROLE_ALTERAR_VOUCHER"),
    CADASTRAR_VOUCHER(24, "Cadastrar Voucher", "ROLE_CADASTRAR_VOUCHER"),
    APAGAR_VOUCHER(25, "Apagar Voucher", "ROLE_APAGAR_VOUCHER"),

    //Permissão Empresa
    BUSCAR_EMPRESA(26, "Buscar Empresa", "ROLE_BUSCAR_EMPRESA"),
    ALTERAR_EMPRESA(27, "Editar Emporesa", "ROLE_ALTERAR_EMPRESA"),
    CADASTRAR_EMPRESA(28, "Cadastrar Empresa", "ROLE_CADASTRAR_EMPRESA"),
    APAGAR_EMPRESA(29, "Apagar Empresa", "ROLE_APAGAR_EMPRESA");


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

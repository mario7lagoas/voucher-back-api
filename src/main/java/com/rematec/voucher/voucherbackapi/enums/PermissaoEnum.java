package com.rematec.voucher.voucherbackapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PermissaoEnum {

    //Permissão Lojas
    MENU_LOJA(1, "Menu Loja", "ROLE_MENU_LOJA"),
    BUSCAR_LOJA(2, "Buscar Loja", "ROLE_BUSCAR_LOJA"),
    ALTERAR_LOJA(3, "Editar Loja", "ROLE_ALTERAR_LOJA"),
    CADASTRAR_LOJA(4, "Cadastrar Loja", "ROLE_CADASTRAR_LOJA"),
    APAGAR_LOJA(5, "Apagar Loja", "ROLE_APAGAR_LOJA"),


    //Permissão Usuarios
    MENU_USUARIO(6, "Menu Usario", "ROLE_MENU_USUARIO"),
    BUSCAR_USUARIO(7, "Buscar Usuário", "ROLE_BUSCAR_USUARIO"),
    ALTERAR_USUARIO(8, "Editar Usuário", "ROLE_ALTERAR_USUARIO"),
    CADASTRAR_USUARIO(9, "Cadastrar Usuário", "ROLE_CADASTRAR_USUARIO"),
    APAGAR_USUARIO(10, "Apagar Usuario", "ROLE_APAGAR_USUARIO"),

    //Permissão Promoção
    MENU_PROMOCAO(11, "Menu Promoção", "ROLE_MENU_PROMOCAO"),
    BUSCAR_PROMOCAO(12, "Buscar Promoção", "ROLE_BUSCAR_PROMOCAO"),
    ALTERAR_PROMOCAO(13, "Editar Promoção", "ROLE_ALTERAR_PROMOCAO"),
    CADASTRAR_PROMOCAO(14, "Cadastrar Promoção", "ROLE_CADASTRAR_PROMOCAO"),
    APAGAR_PROMOCAO(15, "Apagar Promoção", "ROLE_APAGAR_PROMOCAO"),

    //Permissão Perfis
    MENU_PERFIL(16, "Menu Perfil", "ROLE_MENU_PERFIL"),
    BUSCAR_PERFIL(17, "Buscar Perfil", "ROLE_BUSCAR_PERFIL"),
    ALTERAR_PERFIL(18, "Editar Perfil", "ROLE_ALTERAR_PERFIL"),
    CADASTRAR_PERFIL(19, "Cadastrar Perfil", "ROLE_CADASTRAR_PERFIL"),
    APAGAR_PERFIL(20, "Apagar Perfil", "ROLE_APAGAR_PERFIL"),

    //Permissão Voucher
    MENU_VOUCHER(21, "Menu Transacao", "ROLE_MENU_VOUCHER"),
    BUSCAR_VOUCHER(22, "Buscar Voucher", "ROLE_BUSCAR_VOUCHER"),
    ALTERAR_VOUCHER(23, "Editar Voucher", "ROLE_ALTERAR_VOUCHER"),
    CADASTRAR_VOUCHER(24, "Cadastrar Voucher", "ROLE_CADASTRAR_VOUCHER"),
    APAGAR_VOUCHER(25, "Apagar Voucher", "ROLE_APAGAR_VOUCHER"),

    //Permissão Empresa
    MENU_EMPRESA(26, "Menu Empresa", "ROLE_MENU_EMPRESA"),
    BUSCAR_EMPRESA(27, "Buscar Empresa", "ROLE_BUSCAR_EMPRESA"),
    ALTERAR_EMPRESA(28, "Editar Emporesa", "ROLE_ALTERAR_EMPRESA"),
    CADASTRAR_EMPRESA(29, "Cadastrar Empresa", "ROLE_CADASTRAR_EMPRESA"),
    APAGAR_EMPRESA(30, "Apagar Empresa", "ROLE_APAGAR_EMPRESA");


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

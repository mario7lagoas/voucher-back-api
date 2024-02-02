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
    USUARIO(5,"Usuario", "ROLE_USUARIO");
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

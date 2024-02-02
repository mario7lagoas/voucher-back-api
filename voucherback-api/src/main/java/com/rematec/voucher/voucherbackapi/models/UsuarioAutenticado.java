package com.rematec.voucher.voucherbackapi.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioAutenticado {

    private String email;
    private String password;
    private  String userName;

}

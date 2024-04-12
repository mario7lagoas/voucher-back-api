package com.rematec.voucher.voucherbackapi.exceptios;

public class UsuarioInativoException extends RuntimeException{
    public UsuarioInativoException(){
        super();
    }

    public UsuarioInativoException(String mensagem){
        super(mensagem);
    }

}

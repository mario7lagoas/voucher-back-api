package com.rematec.voucher.voucherbackapi.exceptios;

public class UsuarioCadastradoException extends RuntimeException{
    public UsuarioCadastradoException(){
        super();
    }

    public UsuarioCadastradoException(String mensagem){
        super(mensagem);
    }

}

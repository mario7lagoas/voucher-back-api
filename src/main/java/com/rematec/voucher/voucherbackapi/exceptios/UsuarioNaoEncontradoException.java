package com.rematec.voucher.voucherbackapi.exceptios;

public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(){
        super();
    }

    public UsuarioNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}

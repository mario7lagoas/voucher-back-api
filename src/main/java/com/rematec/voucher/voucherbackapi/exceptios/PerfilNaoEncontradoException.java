package com.rematec.voucher.voucherbackapi.exceptios;

public class PerfilNaoEncontradoException extends RuntimeException{
    public PerfilNaoEncontradoException(){
        super();
    }
    public PerfilNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}

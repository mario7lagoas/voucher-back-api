package com.rematec.voucher.voucherbackapi.exceptios;

public class PromocaoNaoEncontradaException extends RuntimeException{
    public PromocaoNaoEncontradaException(){
        super();
    }
    public PromocaoNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}

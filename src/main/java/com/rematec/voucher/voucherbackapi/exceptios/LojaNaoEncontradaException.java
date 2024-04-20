package com.rematec.voucher.voucherbackapi.exceptios;

public class LojaNaoEncontradaException extends RuntimeException{
    public LojaNaoEncontradaException(){
        super();
    }

    public LojaNaoEncontradaException(String mensagem){
        super(mensagem);
    }

}

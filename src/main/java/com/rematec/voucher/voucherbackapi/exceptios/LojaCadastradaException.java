package com.rematec.voucher.voucherbackapi.exceptios;

public class LojaCadastradaException extends RuntimeException{
    public LojaCadastradaException(){
        super();
    }

    public LojaCadastradaException(String mensagem){
        super(mensagem);
    }

}

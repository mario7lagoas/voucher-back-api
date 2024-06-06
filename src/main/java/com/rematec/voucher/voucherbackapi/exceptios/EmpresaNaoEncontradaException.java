package com.rematec.voucher.voucherbackapi.exceptios;

public class EmpresaNaoEncontradaException extends RuntimeException{
    public EmpresaNaoEncontradaException(){
        super();
    }
    public EmpresaNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}

package com.rematec.voucher.voucherbackapi.exceptios;

public class EmpresaCadastradaException extends RuntimeException{

    public EmpresaCadastradaException(){
        super();
    }

    public EmpresaCadastradaException(String mensagem){
        super(mensagem);
    }
}

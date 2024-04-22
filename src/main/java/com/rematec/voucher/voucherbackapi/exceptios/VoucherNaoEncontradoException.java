package com.rematec.voucher.voucherbackapi.exceptios;

public class VoucherNaoEncontradoException extends RuntimeException{
    public VoucherNaoEncontradoException(){
        super();
    }

    public VoucherNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}

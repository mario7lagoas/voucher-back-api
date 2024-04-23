package com.rematec.voucher.voucherbackapi.exceptios;

public class VoucherEmUsoException extends RuntimeException{
    public VoucherEmUsoException(){
        super();
    }

    public VoucherEmUsoException(String mensagem){
        super(mensagem);
    }
}

package com.rematec.voucher.voucherbackapi.exceptios;

public class VoucherUtilizadoException extends RuntimeException{
    public VoucherUtilizadoException(){
        super();
    }

    public VoucherUtilizadoException(String mensagem){
        super(mensagem);
    }
}

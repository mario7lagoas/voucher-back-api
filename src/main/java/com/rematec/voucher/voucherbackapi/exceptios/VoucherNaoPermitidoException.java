package com.rematec.voucher.voucherbackapi.exceptios;

public class VoucherNaoPermitidoException extends RuntimeException{
    public VoucherNaoPermitidoException(){
        super();
    }

    public VoucherNaoPermitidoException(String mensagem){
        super(mensagem);
    }
}

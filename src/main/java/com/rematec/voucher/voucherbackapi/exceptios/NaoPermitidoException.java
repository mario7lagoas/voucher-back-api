package com.rematec.voucher.voucherbackapi.exceptios;

public class NaoPermitidoException extends RuntimeException{
    public NaoPermitidoException(){
        super();
    }

    public NaoPermitidoException(String mensagem){
        super(mensagem);
    }

}

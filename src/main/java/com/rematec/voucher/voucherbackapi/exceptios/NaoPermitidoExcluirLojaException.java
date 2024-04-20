package com.rematec.voucher.voucherbackapi.exceptios;

public class NaoPermitidoExcluirLojaException extends RuntimeException{
    public NaoPermitidoExcluirLojaException(){
        super();
    }

    public NaoPermitidoExcluirLojaException(String mensagem){
        super(mensagem);
    }

}

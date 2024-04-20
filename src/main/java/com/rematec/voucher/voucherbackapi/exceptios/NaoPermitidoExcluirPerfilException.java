package com.rematec.voucher.voucherbackapi.exceptios;

public class NaoPermitidoExcluirPerfilException extends RuntimeException{
    public NaoPermitidoExcluirPerfilException(){
        super();
    }

    public NaoPermitidoExcluirPerfilException(String mensagem){
        super(mensagem);
    }

}

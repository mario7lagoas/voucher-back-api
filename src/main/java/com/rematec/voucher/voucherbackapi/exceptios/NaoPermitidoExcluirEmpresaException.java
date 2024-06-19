package com.rematec.voucher.voucherbackapi.exceptios;

public class NaoPermitidoExcluirEmpresaException extends RuntimeException{
    public NaoPermitidoExcluirEmpresaException(){
        super();
    }

    public NaoPermitidoExcluirEmpresaException(String mensagem){
        super(mensagem);
    }

}

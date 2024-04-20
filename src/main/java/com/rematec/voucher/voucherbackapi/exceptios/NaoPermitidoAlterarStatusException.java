package com.rematec.voucher.voucherbackapi.exceptios;

public class NaoPermitidoAlterarStatusException extends RuntimeException{
    public NaoPermitidoAlterarStatusException(){
        super();
    }

    public NaoPermitidoAlterarStatusException(String mensagem){
        super(mensagem);
    }

}

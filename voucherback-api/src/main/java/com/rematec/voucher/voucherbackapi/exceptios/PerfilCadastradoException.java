package com.rematec.voucher.voucherbackapi.exceptios;

public class PerfilCadastradoException extends RuntimeException{
    public PerfilCadastradoException(){
        super();
    }

    public PerfilCadastradoException(String mensagem){
        super(mensagem);
    }

}

package com.rematec.voucher.voucherbackapi.exceptios;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super();
    }

    public BadRequestException(String mensagem){
        super(mensagem);
    }

}

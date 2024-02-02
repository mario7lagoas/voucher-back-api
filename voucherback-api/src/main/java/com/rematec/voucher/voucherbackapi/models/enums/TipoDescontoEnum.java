package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.Getter;

@Getter
public enum TipoDescontoEnum {

    VALOR("Desconto em Valor"), PERCENTUAL("Desconto Percentual");

    private String nome;

    TipoDescontoEnum(String nome){
        this.nome = nome;
    }

    public String toString(){
        return nome;
    }

}

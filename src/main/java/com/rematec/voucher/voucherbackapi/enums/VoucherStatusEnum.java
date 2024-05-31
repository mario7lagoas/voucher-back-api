package com.rematec.voucher.voucherbackapi.enums;

import lombok.Getter;

@Getter
public enum VoucherStatusEnum {

    DISPONIBILIZADO("Disponibilizado"), CANCELADO("Cancelado"), CONFIRMADO("Confirmado"),
    UTILIZADO("Utilizado"), EXPIRADO("Expirado");

    private String nome;

    VoucherStatusEnum(String nome) {
        this.nome = nome;
    }

    public String toString() {
        return nome;
    }

}

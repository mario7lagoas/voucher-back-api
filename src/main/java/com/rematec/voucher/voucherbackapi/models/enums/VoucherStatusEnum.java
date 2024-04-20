package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.Getter;

@Getter
public enum VoucherStatusEnum {

    DISPONIBILIZADO("Disponibilizado"), CANCELADO("Cancelado"), CONFIRMADO("Confirmado"),
    UTILIZADO("Utilizado");

    private String nome;

    VoucherStatusEnum(String nome) {
        this.nome = nome;
    }

    public String toString() {
        return nome;
    }

}

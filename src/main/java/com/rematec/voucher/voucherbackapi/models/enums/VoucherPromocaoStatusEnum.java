package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.Getter;

@Getter
public enum VoucherPromocaoStatusEnum {
    DISPONIVEL("Disponível"), EM_USO("Em uso"), UTILIZADO("Utilizado");

    private String nome;

    VoucherPromocaoStatusEnum(String nome) {
        this.nome = nome;
    }

    public String toString() {
        return nome;
    }
}

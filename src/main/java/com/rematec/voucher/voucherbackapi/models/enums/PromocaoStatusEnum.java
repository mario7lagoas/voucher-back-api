package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.Getter;

@Getter
public enum PromocaoStatusEnum {
    ATIVA("Ativa"), PROGRESSO("Em progresso"), BLOQUEADA("Bloqueada"), FINALIZADA("Finalizada");

    private String nome;

    PromocaoStatusEnum(String nome) {
        this.nome = nome;
    }

    public String toString() {
        return nome;
    }

}

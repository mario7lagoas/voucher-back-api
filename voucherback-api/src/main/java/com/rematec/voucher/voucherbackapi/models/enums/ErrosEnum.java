package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.Getter;

@Getter

public enum ErrosEnum {
    NAO_PERMITIDO_EXCLUIR("Não permitido Excluir."),
    NAO_ENCONTRADA("Não encontrado."),
    NAO_PERMITIDO_ALTERAR_STATUS("Não permitido alterar Status."),
    USUARIO_NAO_ENCONTRADO("Usuario não encontrado."),
    ENDPOINT_NAO_ENCONTRADO("Endpoint não encontrado.");

    private String nome;

    ErrosEnum(String nome){
        this.nome = nome;
    }

    public String toString(){
        return nome;
    }
}

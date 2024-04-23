package com.rematec.voucher.voucherbackapi.models.enums;

import lombok.Getter;

@Getter

public enum ErrosEnum {
    NAO_PERMITIDO_EXCLUIR("Não permitido Excluir."),
    NAO_ENCONTRADO("Não encontrado."),
    NAO_PERMITIDO_ALTERAR_STATUS("Não permitido alterar Status."),
    USUARIO_NAO_ENCONTRADO("Usuario não encontrado."),
    USUARIO_INATIVADO("Usuario Inativado."),
    CNPJ_JA_CADASTRADO("CNPJ já Cadastrado."),
    USUARIO_JA_CADASTRADO("Usuario já Cadastrado."),
    PERFIL_JA_CADASTRADO("Perfil já cadastrado."),
    EM_USO("Em uso no momento."),
    UTILIZADO("Já utilizado."),
    NAO_PERMITIDO("Não permitido."),
    ENDPOINT_NAO_ENCONTRADO("Endpoint não encontrado.");

    private String nome;

    ErrosEnum(String nome){
        this.nome = nome;
    }

    public String toString(){
        return nome;
    }
}

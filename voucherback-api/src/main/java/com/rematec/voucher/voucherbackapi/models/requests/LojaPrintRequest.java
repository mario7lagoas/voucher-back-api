package com.rematec.voucher.voucherbackapi.models.requests;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LojaPrintRequest {
    private String guid;
    private String nome;
    private String cnpj;
    private String identificacao;
    private Boolean status;
    private String dataCadastro;
}

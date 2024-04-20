package com.rematec.voucher.voucherbackapi.models.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LojaPrintRequest extends Guid {
    private String nome;
    private String cnpj;
    private String identificacao;
    private Boolean status;
    private String dataCadastro;
}

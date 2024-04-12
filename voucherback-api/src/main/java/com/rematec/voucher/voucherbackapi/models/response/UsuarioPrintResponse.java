package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UsuarioPrintResponse {
    private String nome;
    private String email;
    private String status;
    private String dataCadastro;
    private String dataAtualizacao;
    private List<String> perfis ;
}

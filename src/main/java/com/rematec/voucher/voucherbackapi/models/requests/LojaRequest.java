package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LojaRequest {

    @NotNull(message = "Nome da loja Obrigatorio")
    @NotBlank(message = "Nome da loja Obrigatorio")
    private String nome;
    @NotNull(message = "CNPJ da loja Obrigatorio")
    @NotBlank(message = "CNPJ da loja Obrigatorio")
    private String cnpj;
    @NotNull(message = "Identificacao da loja Obrigatorio")
    @NotBlank(message = "Identificação Obrigatorio")
    private String identificacao;

    @NotNull(message = "Status da loja é Obrigatorio")
    private Boolean status;
}

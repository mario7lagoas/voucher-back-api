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
public class ConsultaVoucherRequest {
    @NotNull(message = "CNPJ da loja não pode ser nulo.")
    @NotBlank(message = "CNPJ da loja Obrigatorio")
    private String filialCnpj;
    @NotNull(message = "Numero do PDV não pode ser nulo.")
    @NotBlank(message = "Numero do PDV Obrigatorio")
    private String pdvFilial;
    @NotNull(message = "CPF do Cliente não pode ser nulo.")
    @NotBlank(message = "CPF do Cliente Obrigatorio")
    private String clienteCpf;
    @NotNull(message = "Valor da compra.")
    private Double valorCompra;
}
